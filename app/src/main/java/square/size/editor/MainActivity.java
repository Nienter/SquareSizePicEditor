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
    @BindView(R.id.frame4)
    FrameLayout frame4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.fl_gallery)
    public void onFrame1() {
        EasyPhotoUtils.startSingle(this);
    }

    @OnClick(R.id.fl_camera)
    public void onFrame2() {
        EasyPhotoUtils.startCamera(this);
    }

    @OnClick(R.id.fl_collage)
    public void onFrame3() {

    }

    @OnClick(R.id.frame4)
    public void onFrame4() {

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
