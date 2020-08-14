package square.size.editor;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.qw.soul.permission.bean.Permission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final int CAMERA_REQUEST_CODE = 1001;

    @BindView(R.id.fl_gallery)
    FrameLayout fl_gallery;
    @BindView(R.id.fl_camera)
    FrameLayout fl_camera;
    @BindView(R.id.fl_collage)
    FrameLayout fl_collage;
    @BindView(R.id.fl_about)
    FrameLayout fl_about;
    @BindView(R.id.fl_setting)
    FrameLayout fl_setting;

    @Override
    protected int attachLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.fl_gallery)
    public void onFlGallery() {
        Constants.from = Constants.GALLERY;
        checkPer(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @OnClick(R.id.fl_camera)
    public void onFlCamera() {
//        EasyPhotoUtils.startCamera(this);
        checkPer(Manifest.permission.CAMERA);
    }

    @OnClick(R.id.fl_collage)
    public void onFlCollage() {
        Constants.from = Constants.COLLAGE;
        checkPer(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @OnClick(R.id.fl_setting)
    public void onFlSetting() {

    }

    @OnClick(R.id.fl_about)
    public void setFl_about() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void PermissionOk(Permission permission) {
        super.PermissionOk(permission);
        switch (permission.permissionName) {
            case Manifest.permission.CAMERA:
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                goHandle();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1 && data != null) {
//                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
//                if (resultPhotos != null && resultPhotos.size() > 0) {
//                    Photo photo = resultPhotos.get(0);
//                    Log.d(TAG, "onActivityResult: " + photo.path);
//                    goHandle(photo);
//                }
//            }
//        }
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                Log.d(TAG, "onActivityResult: " + bitmap.getWidth());
                Intent intent = new Intent(this, HandleActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("borderSize", 1);
                intent.putExtra("themeId", 0);
                List<Bitmap> bitmaps = new ArrayList<>();
                bitmaps.add(bitmap);
                Constants.setCacheBitmaps(bitmaps);
                startActivity(intent);
                finish();
            }
        }
    }

    private void goHandle() {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, ExitActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
