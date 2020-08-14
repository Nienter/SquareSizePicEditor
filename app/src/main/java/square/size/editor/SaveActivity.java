package square.size.editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.itheima.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaveActivity extends BaseActivity {
    @BindView(R.id.bt_back)
    AppCompatImageButton bt_back;
    @BindView(R.id.bt_next)
    AppCompatImageButton bt_next;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;
    @BindView(R.id.rv)
    RoundedImageView roundedImageView;

    @Override
    protected int attachLayout() {
        return R.layout.activity_save;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tv_title.setText(getString(R.string.save));
        String path = getIntent().getStringExtra("path");
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        roundedImageView.setImageBitmap(bitmap);

    }

    @OnClick(R.id.bt_back)
    public void setBt_back() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.bt_next)
    public void setBt_next() {
        File file = new File(Objects.requireNonNull(getIntent().getStringExtra("path")));
        if (file.exists()) {
            Tools.updateFileFromDatabase(this, file.getPath());
            file.delete();
        }
        goMain();
    }
}
