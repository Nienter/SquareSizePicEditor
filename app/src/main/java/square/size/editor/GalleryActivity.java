package square.size.editor;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import square.size.editor.adapter.AlbumAdapter;

public class GalleryActivity extends BaseActivity {
    private ProgressDialog progressDialog = new ProgressDialog(this);
    @BindView(R.id.bt_back)
    AppCompatImageButton bt_back;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;
    @BindView(R.id.bt_next)
    AppCompatImageButton bt_next;

    @BindView(R.id.ry_album)
    RecyclerView ry_album;

    private List<String> mDirPaths = new ArrayList<>();
    private List<Album> mImageFolders = new ArrayList<>();
    private AlbumAdapter albumAdapter;

    @Override
    protected int attachLayout() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        tv_title.setText(getString(R.string.gallery));

        getAlbum();
    }

    private void getAlbum() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        showLoad();
        new WorkThread().postTask(new WorkThread.ITask() {
            @Override
            public Object onDo(Object... args) {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = GalleryActivity.this.getContentResolver();
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png", "image/jpg"},
                        MediaStore.Images.Media.DATE_TAKEN + " DESC");//获取图片的cursor，按照时间倒序（发现没卵用)
                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));// 1.获取图片的路径
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;//不获取sd卡根目录下的图片
                    String parentPath = parentFile.getAbsolutePath();//2.获取图片的文件夹信息
                    String parentName = parentFile.getName();
                    Album imageFloder;//自定义一个model，来保存图片的信息

                    //这个操作，可以提高查询的效率，将查询的每一个图片的文件夹的路径保存到集合中，
                    //如果存在，就直接查询下一个，避免对每一个文件夹进行查询操作
                    if (mDirPaths.contains(parentPath)) {
                        continue;
                    } else {
                        mDirPaths.add(parentPath);//将父路径添加到集合中
                        imageFloder = new Album();
                        imageFloder.setFirstImagePath(path);
                        imageFloder.setDirPath(parentPath);
                        imageFloder.setDirName(parentName);
                    }
                    List<String> strings = null;
                    try {
                        strings = Arrays.asList(parentFile.list(getFileterImage()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int picSize = strings.size();//获取每个文件夹下的图片个数
                    imageFloder.setPicNum(picSize);//传入每个相册的图片个数
                    mImageFolders.add(imageFloder);//添加每一个相册
                    //获取图片最多的文件夹信息（父目录对象和个数，使得刚开始显示的是最多图片的相册
//                    if (picSize > mPicsSize) {
//                        mPicsSize = picSize;
//                        mImgDir = parentFile;
//                    }
                }

                return null;
            }

            @Override
            public void onResult(Object ret) {
                albumAdapter = new AlbumAdapter(mImageFolders);
                ry_album.setLayoutManager(new GridLayoutManager(GalleryActivity.this, 3));
                ry_album.setAdapter(albumAdapter);
            }
        });

    }

    private FilenameFilter getFileterImage() {
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg")
                        || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        };
        return filenameFilter;
    }

    public void showLoad() {
        progressDialog.show();
    }

    public void dismissLoad() {
        progressDialog.dismiss();
    }

    @OnClick(R.id.bt_back)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.bt_next)
    public void onNext() {
        Intent intent = new Intent(this, HandleActivity.class);
        startActivity(intent);
        finish();
    }
}
