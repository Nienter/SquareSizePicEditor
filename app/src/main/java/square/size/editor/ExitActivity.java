package square.size.editor;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExitActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_exit)
    TextView exit;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @Override
    protected int attachLayout() {
        return R.layout.activity_finish;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tv_cancel.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_exit) {
            finish();
            System.exit(0);
        } else if (v.getId() == R.id.tv_cancel) {
            Intent intent = new Intent(this, MainActivity.class);
            start(intent, true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            start(intent, true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public String setAdSize() {
//        return "medium";
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }

    public void start(Intent intent, boolean isFinish) {
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }
}
