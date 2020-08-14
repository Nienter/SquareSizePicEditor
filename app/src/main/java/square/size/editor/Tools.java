package square.size.editor;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import square.size.editor.puzz.view.PuzzleView;

public class Tools {
    private static final String TAG = "Tools";

    public static int getWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (int) (dm.widthPixels);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static Bitmap resize(Bitmap b, float x, float y, View view) {
        float w = b.getWidth();
        float h = b.getHeight();
        Log.d(TAG, "resize: " + w + "***" + h);

        float sx = (float) x / w;
        float sy = (float) y / h;
        int midx = view.getBottom() / 2;
        int midy = view.getRight() / 2;
        int startX = (int) (midy - (sy / 2));
        int startY = (int) (midx - (sx / 2));
        Matrix matrix = new Matrix();
        //也可以按两者之间最大的比例来设置放大比例，这样不会是图片压缩
//        float bigerS = Math.max(sx,sy);
//        matrix.postScale(bigerS,bigerS);
        matrix.postScale(sy, sy); // 长和宽放大缩小的比例
//        matrix.postTranslate(sy / 2, sy / 2);
        Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, (int) w,
                (int) h, matrix, true);
        return resizeBmp;
    }

    public static Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }

//    public static void setSelec(View view, String color) {
//        int strokeWidth = 5;
//        int strokeColor = Color.parseColor("#FF5887");
//        int fillColor = Color.parseColor(color); //内部填充颜色
//        GradientDrawable gd = new GradientDrawable();
//        gd.setGradientType(GradientDrawable.OVAL);
//        gd.setColor(fillColor);
//        gd.setStroke(strokeWidth, strokeColor);
//        view.setBackground(gd);
//
//    }
//    public static void setUnSelec(View view, String color) {
//        int strokeWidth = 1;
//        int fillColor = Color.parseColor(color);
//        GradientDrawable gd = new GradientDrawable();
//        gd.setGradientType(GradientDrawable.OVAL);
//        gd.setColor(fillColor);
//        view.setBackgroundDrawable(gd);
//
//    }


    public static File uriToFile(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }
    public static void updateFileFromDatabase(Context context,String filepath){
        String where=MediaStore.Audio.Media.DATA+" like \""+filepath+"%"+"\"";
        int i=  context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,where,null);
        if(i>0){
            Log.e(TAG,"媒体库更新成功！");
        }
    }


    public static File getNewFile(Context context, String folderName) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);

        String timeStamp = simpleDateFormat.format(new Date());

        String path;
        if (isSDAvailable()) {
            path = getFolderName(folderName) + File.separator + timeStamp + ".jpg";
        } else {
            path = context.getFilesDir().getPath() + File.separator + timeStamp + ".jpg";
        }

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        return new File(path);
    }
    public static Bitmap createBitmap(PuzzleView puzzleView) {
        puzzleView.clearHandling();

        puzzleView.invalidate();

        Bitmap bitmap =
                Bitmap.createBitmap(puzzleView.getWidth(), puzzleView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        puzzleView.draw(canvas);

        return bitmap;
    }
    private static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    public static String getFolderName(String name) {
        File mediaStorageDir =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        name);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return "";
            }
        }

        return mediaStorageDir.getAbsolutePath();
    }
    public static void savePuzzle(PuzzleView puzzleView, File file, int quality, Callback callback) {
        Bitmap bitmap = null;
        FileOutputStream outputStream = null;

        try {
            bitmap = createBitmap(puzzleView);
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            if (!file.exists()) {
                Log.e(TAG, "notifySystemGallery: the file do not exist.");
                return;
            }
//
//            try {
////                MediaStore.Images.Media.insertImage(puzzleView.getContext().getContentResolver(),
////                        file.getAbsolutePath(), file.getName(), null);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            puzzleView.getContext()
                    .sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

            if (callback != null) {
                callback.onSuccess();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailed();
            }
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public interface Callback{
        void onSuccess();

        void onFailed();
    }
}
