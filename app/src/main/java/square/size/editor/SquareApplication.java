package square.size.editor;

import com.x.codeX.XApp;
import com.x.codeX.agent.utils.SpUtil;

import resut.AppExtResManager;

public class SquareApplication extends XApp {
    static {
        if(!sDebug){
            System.loadLibrary("my");
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
//        SdkUtils.addSdkPlugin(new AdSdk());
//        if (SpUtil.mPrefSp().getBoolean("isSdkInit", false)) {
//            new AppExtResManager(getApplicationContext()).init1(this);
//            SdkUtils.init();
//            SpUtil.mPrefSp().put("isSdkInit", true);
//        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
