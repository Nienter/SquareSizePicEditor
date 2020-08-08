package com.x.codeX;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.x.codeX.agent.utils.SpUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class XApp extends Application {
    public static Application app;
    public static final String ACTIVIT_THREAD = "android.app.ActivityThread";
    public static final String CURRENT_ACTIVITY_THREAD = "currentActivityThread";
    public static final String INSTRUMENTATION = "mInstrumentation";
    public final static boolean sDebug = true;
    public static boolean isOrigin;
    private static volatile Handler sUIHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        app = this;
        try {
            attachContext(); // hook startActivity
        } catch (Exception e) {
            e.printStackTrace();
        }
//            FlurryUtils.getInstance().init(this, flurryId());
//            AnalyticsProvider.init(app);
//            AudienceNetworkAds.initialize(this);
//            setAppFirstIns();
//            checkAppClude();
//            setWebViewProcess();
//            checkUserType();
//            WorkAlive.init(app);
//            ActivityMonitor.getActivityMonitor().register(app);

    }

    private void checkUserType() {
        boolean haschecked = SpUtil.mPrefSp().getBoolean("checkedattr", false);
        if (haschecked) {
            isOrigin = SpUtil.mPrefSp().getBoolean("natural", false);
            return;
        }
        SpUtil.mPrefSp().put("checkedattr", true);
        final InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(app).build();
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
                    if (sDebug) err.printStackTrace();
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
            }
        });
    }

    private void setWebViewProcess() {
        getUIHandler().post(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                String processName = getProcessName(app);
                if (!app.getPackageName().equals(processName)) {//判断不等于默认进程名称
                    WebView.setDataDirectorySuffix(processName);
                }
            }
        });
    }

    private String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    public static void checkInstallReferrer(String referrer) {
        isOrigin = referrer.toLowerCase().contains("organic")
                || Build.VERSION.SDK_INT > Build.VERSION_CODES.P;//Android 10
        if (sDebug) {
            Log.d("Cloak", "for DBG, change organic from " + isOrigin + " to false");
            isOrigin = false;
        }
        SpUtil.mPrefSp().put("natural", isOrigin);
        Map<String, String> params = new HashMap<>();
        params.put("ref", referrer);
        AnalyticsFacade.logEvent("install_attr", params);
    }


    public static String getAppName() {
        String name = "";
        try {
            PackageManager pm = app.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(app.getPackageName(), 0);
            name = (String) pm.getApplicationLabel(appInfo);

        } catch (Throwable err) {
        }
        return name;
    }


    public static Handler getUIHandler() {
        if (sUIHandler == null) {
            synchronized (XApp.class) {
                if (sUIHandler == null) {
                    sUIHandler = new Handler(Looper.getMainLooper());
                }
            }
        }

        return sUIHandler;
    }


//    protected abstract String flurryId();

    public static void attachContext() throws Exception {
        //获取当前的ActivityThread对象
        Class<?> activityThreadClass = Class.forName(ACTIVIT_THREAD);
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod(CURRENT_ACTIVITY_THREAD);
        currentActivityThreadMethod.setAccessible(true);
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);
        //拿到在ActivityThread类里面的原始mInstrumentation对象
        Field mInstrumentationField = activityThreadClass.getDeclaredField(INSTRUMENTATION);
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);
        Instrumentation evilInstrumentation = new InstrumentationProxy(mInstrumentation);
        mInstrumentationField.set(currentActivityThread, evilInstrumentation);
    }

    public static Context mContext;

    public static Context getContext() {
        return mContext;
    }
}
