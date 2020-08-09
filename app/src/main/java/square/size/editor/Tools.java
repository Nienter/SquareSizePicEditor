package square.size.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

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
        Log.d(TAG, "resize: "+w+"***"+h);

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
                (int)h, matrix, true);
        return resizeBmp;
    }
}
