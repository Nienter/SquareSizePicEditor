package com.x.codeX;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.facebook.ads.AudienceNetworkAds;
import com.x.codeX.agent.ActivityMonitor;
import com.x.codeX.agent.FlurryUtils;
import com.x.codeX.agent.keepLive.WorkAlive;
import com.x.codeX.agent.utils.SpUtil;
import com.x.codeX.agent.utils.XUtils;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.x.codeX.agent.utils.XUtils.checkAppClude;
import static com.x.codeX.agent.utils.XUtils.setAppFirstIns;

public class MyTools {
    public static void start() {
        FlurryUtils.getInstance().init(XApp.getContext(), XApp.sDebug?"K55S8BG47DZ5JWVW62V3":"N3XSZQ9YJC8GFQV6VPB7");
        AudienceNetworkAds.initialize(XApp.getContext());
        AnalyticsProvider.init(XApp.app);
        setAppFirstIns();
        checkAppClude();
        setWebViewProcess();
        checkUserType();
           /* final Handler handler = new Handler();
            handler.postDelayed(() -> {
                DaemonEnv.initialize(
                        app,  //Application Context.
                        WorkService.class, //刚才创建的 Service 对应的 Class 对象.
                        1000*60*2);//定时唤醒的时间间隔(ms), 默认 6 分钟.
                app.startService(new Intent(app,WorkService.class));
            },1000*60*2);*/
        WorkAlive.init(XApp.getContext());
         XUtils.addShort(XApp.getContext());
        ActivityMonitor.getActivityMonitor().register(XApp.app);
    }

    private static void setWebViewProcess() {
        new Handler().post(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                String processName = getProcessName(getApplicationContext());
                if (!getApplicationContext().getPackageName().equals(processName)) {//判断不等于默认进程名称
                    WebView.setDataDirectorySuffix(processName);
                }
            }
        });
    }

    private static String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    public static boolean isOrigin;

    private static void checkUserType() {
        boolean haschecked = SpUtil.mPrefSp().getBoolean("checkedattr", false);
        if (haschecked) {
            isOrigin = SpUtil.mPrefSp().getBoolean("natural", false);
            return;
        }
        SpUtil.mPrefSp().put("checkedattr", true);
        final InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(getApplicationContext()).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                try {
                    if (responseCode == InstallReferrerClient.InstallReferrerResponse.OK) {
                        ReferrerDetails referrerDetails = referrerClient.getInstallReferrer();
                        String referrer = referrerDetails.getInstallReferrer();
                        checkInstallReferrer(referrer);
                    }
                    referrerClient.endConnection();
                } catch (Throwable err) {
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
            }
        });
    }

    public static void checkInstallReferrer(String referrer) {
        isOrigin = referrer.toLowerCase().contains("organic")
                || Build.VERSION.SDK_INT > Build.VERSION_CODES.P;//Android 10
        if (BuildConfig.DEBUG) {
            Log.d("Cloak", "for DBG, change organic from " + isOrigin + " to false");
            isOrigin = false;
        }
        SpUtil.mPrefSp().put("natural", isOrigin);

        Map<String, String> params = new HashMap<>();
        params.put("ref", referrer);
        AnalyticsFacade.logEvent("install_attr", params);
    }
}
