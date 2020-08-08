package com.x.codeX;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.appevents.AppEventsLogger;
import com.flurry.android.FlurryAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;


public class AnalyticsFacade {
    public static void logEvent(String name, Map<String, String> params){
        if(name!=null){
            EvtBean event = new EvtBean();
            event.name = name;
            event.params = params;

            EventBus.getDefault().post(event);
        }
    }
}

final class EvtBean {
    String name;
    Map<String, String> params;
}


final class AnalyticsProvider {
    private static AnalyticsProvider sProvider;
    public static void init(Application app){
//        Thread.UncaughtExceptionHandler systemHandler = Thread.getDefaultUncaughtExceptionHandler();
//        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
//        });
//
////        //Flurry
////        new FlurryAgent.Builder()
////                .withLogEnabled(AppStart.sDebug)
////                .withCaptureUncaughtExceptions(true)
////                .withContinueSessionMillis(10000)
////                .withLogLevel(Log.VERBOSE)
////                .build(app, flurryKey);
////
////        FacebookSdk.sdkInitialize(app);
////
////        Thread.UncaughtExceptionHandler flurryHandler = Thread.getDefaultUncaughtExceptionHandler();
////        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
////            @Override
////            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
////                if(flurryHandler!=null)
////                    flurryHandler.uncaughtException(thread, throwable);
////
////                if(AppStart.sDebug){
////                    if(systemHandler!=null)
////                        systemHandler.uncaughtException(thread, throwable);
////                }
////            }
////        });

        sProvider = new AnalyticsProvider(app);
    }


    private AppEventsLogger mFbLogger ;
    private AnalyticsProvider(Context context){
        mFbLogger = AppEventsLogger.newLogger(context);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void logEvent(EvtBean entry){
        if(entry==null)
            return;

        String name = entry.name;
        if(TextUtils.isEmpty(name))
            return;

        Map<String, String> params = entry.params;
        if (params != null) {
            FlurryAgent.logEvent(name, params);
        } else {
            FlurryAgent.logEvent(name);
        }

        if (params != null) {
            Bundle bundle = new Bundle();
            for (Map.Entry<String, String> p : params.entrySet()) {
                bundle.putString(p.getKey(), p.getValue());
            }
            mFbLogger.logEvent(name, bundle);

        } else {
            mFbLogger.logEvent(name);
        }

        if(XApp.sDebug){
            Log.d("AnalyticsProvider", "report event:"+entry.name);
        }
    }
}
