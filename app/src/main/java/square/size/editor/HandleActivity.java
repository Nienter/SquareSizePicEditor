package square.size.editor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HandleActivity extends BaseActivity {
    private  static final String TAG = "HandleActivity";
    @BindView(R.id.frame_panel)
    RelativeLayout frame_panel;
    @BindView(R.id.vary)
    VaryImageView varyImageView;
    private Matrix matrix = new Matrix();

    @Override
    protected int attachLayout() {
        return R.layout.activity_handle;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) frame_panel.getLayoutParams();
        layoutParams.width =Tools.getWidth(this);
        layoutParams.height = Tools.getWidth(this);
        frame_panel.setLayoutParams(layoutParams);
        Bitmap resource = BitmapFactory.decodeResource(getResources(), R.drawable.aaa);
        Log.d(TAG, "onCreate: "+resource.getWidth());
        resource = Tools.resize(resource, 0, Tools.getWidth(this)-Tools.dp2px(this,40));
        varyImageView.setBitmap(resource);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goMain();
        }
        return super.onKeyDown(keyCode, event);
    }
}
