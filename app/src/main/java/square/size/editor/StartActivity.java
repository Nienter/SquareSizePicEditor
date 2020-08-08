package square.size.editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import resut.AppExtResManager;

public class StartActivity extends Activity implements WorkThread.ITask {
    private long startTime;    //获取开始时间
    private long endTime;
    private WorkThread myWork = new WorkThread();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        waitAndLoad();
    }

    private void waitAndLoad() {
        startTime = System.currentTimeMillis();
        myWork.postTask(this);
    }

    private void initResource() {
        new AppExtResManager(getApplicationContext()).init1(getApplication());
    }

    private void go() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public Object onDo(Object... args) {
        initResource();
        return null;
    }

    @Override
    public void onResult(Object ret) {
//        if (!SpUtil.mPrefSp().getBoolean("isSdkInit", false)) {
////            SdkUtils.init();
////            SpUtil.mPrefSp().put("isSdkInit", true);
//        }
        endTime = System.currentTimeMillis();
        long total = endTime - startTime;
        if (endTime - startTime > 3000) {
            go();
        } else {
            new Handler().postDelayed(this::go, Math.abs(total));
        }
    }
}
