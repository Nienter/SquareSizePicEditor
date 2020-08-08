package com.x.codeX.agent;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryConfig;
import com.flurry.android.FlurryConfigListener;
import com.x.codeX.XApp;

public class FlurryUtils {
    private static FlurryUtils flurry = new FlurryUtils();
    private static FlurryAgent.Builder builder;
    private static FlurryConfig config;

    public static FlurryUtils getInstance() {
        if (builder == null) {
            synchronized (FlurryUtils.class) {
                if (builder == null) {
                    builder = new FlurryAgent.Builder();
                }
            }
        }
        return flurry;
    }

    public static void init(Context context, String s) {
        builder
                .withLogLevel(Log.VERBOSE)
                .withLogEnabled(true)
                .build(context, s);
        loadFluury();
    }

    private static void loadFluury() {
        config = FlurryConfig.getInstance();
        config.registerListener(new FlurryConfigListener() {
            @Override
            public void onFetchSuccess() {
                if (XApp.sDebug) {
                    Log.d("flurry", "ok");
                }
                config.activateConfig();
            }

            @Override
            public void onFetchNoChange() {
                if (XApp.sDebug) {
                    Log.d("flurry", "onFetchNoChange");
                }
            }

            @Override
            public void onFetchError(boolean b) {
                if (XApp.sDebug) {
                    Log.d("flurry", "error");
                }
            }

            @Override
            public void onActivateComplete(boolean b) {
                if (XApp.sDebug) {
                    Log.d("flurry", "onActivateComplete");
                }
                AdsConfig.getAdsConfig();
            }
        });
        config.fetchConfig();
    }
    public static void Fetch() {
        if (config != null)
            config.fetchConfig();
    }
}
