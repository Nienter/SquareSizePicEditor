package square.size.editor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.bt_back)
    AppCompatImageButton bt_back;
    @BindView(R.id.bt_next)
    AppCompatImageButton bt_next;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;
    @BindView(R.id.version)
    AppCompatTextView tv_version;

    @Override
    protected int attachLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        bt_next.setVisibility(View.GONE);
        tv_version.setText("Version "+getVersion()+"\n"+"@2020 Studio 8 Apps");
    }
    public String getVersion(){
        return BuildConfig.VERSION_NAME;
    }
    @OnClick(R.id.bt_back)
    public void setBt_back() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
