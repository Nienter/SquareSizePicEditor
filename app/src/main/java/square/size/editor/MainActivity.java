package square.size.editor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

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
        EasyPhotoUtils.startSingle(this);
    }

    @OnClick(R.id.fl_camera)
    public void onFlCamera() {
        EasyPhotoUtils.startCamera(this);
    }

    @OnClick(R.id.fl_collage)
    public void onFlCollage() {

    }

    @OnClick(R.id.fl_setting)
    public void onFlSetting() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1 && data != null) {
                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                if (resultPhotos != null && resultPhotos.size() > 0) {
                    Photo photo = resultPhotos.get(0);
                    Log.d(TAG, "onActivityResult: " + photo.path);
                    goHandle(photo);
                }
            }
        }
    }

    private void goHandle(Photo photo) {
        Intent intent = new Intent(this, HandleActivity.class);
//        intent.putExtra("path",photo.path);
        intent.setData(photo.uri);
        startActivity(intent);

        finish();
    }
}
