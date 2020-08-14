package square.size.editor;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CutActivity extends BaseActivity {
    @BindView(R.id.cropImageView)
    CropImageView cropImageView;
    @BindView(R.id.tv_free)
    TextView tv_free;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_2)
    TextView tv_2;
    @BindView(R.id.tv_3)
    TextView tv_3;
    @BindView(R.id.tv_4)
    TextView tv_4;
    @BindView(R.id.tv_5)
    TextView tv_5;
    @BindView(R.id.tv_6)
    TextView tv_6;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;
    @BindView(R.id.bt_next)
    AppCompatImageButton bt_next;
    @BindView(R.id.bt_back)
    AppCompatImageButton bt_back;

    @Override
    protected int attachLayout() {

        return R.layout.activity_cut;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
//        cropImageView.load(Tools.getUriFromDrawableRes(this, get)).execute(new LoadCallback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Toast.makeText(CutActivity.this, "load failed", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
//        Drawable drawable = getDrawable(R.drawable.aaa);
        cropImageView.setImageBitmap(Constants.cache);
        free();
    }

    private void initView() {
        tv_title.setText(getString(R.string.crop));
    }

    public void resetStatus() {
        for (int i = 0; i < ll.getChildCount(); i++) {
            ll.getChildAt(i).setSelected(false);
        }
    }

    @OnClick(R.id.tv_free)
    public void free() {
        cropImageView.setCropMode(CropImageView.CropMode.FREE);
        resetStatus();
        tv_free.setSelected(true);
    }

    @OnClick(R.id.tv_1)
    public void square() {
        cropImageView.setCropMode(CropImageView.CropMode.SQUARE);
        resetStatus();
        tv_1.setSelected(true);
    }

    @OnClick(R.id.tv_2)
    public void tv2() {
        cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3);
        resetStatus();
        tv_2.setSelected(true);
    }

    @OnClick(R.id.tv_3)
    public void tv3() {
        cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4);
        resetStatus();
        tv_3.setSelected(true);
    }

    @OnClick(R.id.tv_4)
    public void tv4() {
        cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
        resetStatus();
        tv_4.setSelected(true);
    }

    @OnClick(R.id.tv_5)
    public void tv5() {
        cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16);
        resetStatus();
        tv_5.setSelected(true);
    }

    @OnClick(R.id.tv_6)
    public void tv6() {
        cropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
        resetStatus();
        tv_6.setSelected(true);
    }

    @OnClick(R.id.bt_next)
    public void next() {
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), Constants.cache, null, null));

        cropImageView.crop(uri).execute(new CropCallback() {
            @Override
            public void onSuccess(Bitmap cropped) {
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                cropped.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                byte[] bytes = baos.toByteArray();
//                Bundle b = new Bundle();
//                b.putByteArray("bitmap", bytes);
//                Intent intent = new Intent();
//                intent.putExtras(b);
                Constants.cache = cropped;
                setResult(-1);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(CutActivity.this, getString(R.string.cutfailed), Toast.LENGTH_LONG).show();
                Constants.cache = null;
                setResult(-2);
                finish();
            }
        });
        File file = null;
        file = Tools.uriToFile(uri, this);
        if (file.exists()) {
            Log.d(TAG, "next: ");
//            file.delete();
            Tools.updateFileFromDatabase(this,file.getPath());
            file.delete();
//            file.delete();
        }
    }

    private static final String TAG = "CutActivity";

    @OnClick(R.id.bt_back)
    public void back() {
        setResult(RESULT_CANCELED);
        finish();
    }

}
