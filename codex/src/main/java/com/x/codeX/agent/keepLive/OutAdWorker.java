package com.x.codeX.agent.keepLive;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.x.codeX.XApp;
import com.x.codeX.agent.utils.XUtils;
import com.x.codexsdk.sa.MainActivity;

public class OutAdWorker extends Worker {
    public OutAdWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            if(XApp.sDebug){
                Log.d("BgWorker", "startWork Time "+System.currentTimeMillis());
            }
            if (XUtils.isetBergAdOut()) {
                if (!XApp.isOrigin) {
                    if (XUtils.isScreenOn()) {
                        if (!XUtils.isAppClude()) {

                            if (XUtils.isMoreTime()) {
                                if (XUtils.getAppIns() >= XUtils.getNewUserAdDelayT() * DateUtils.SECOND_IN_MILLIS) {
                                    Intent intent = new Intent(XApp.app, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    XApp.app.startActivity(intent);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception err) {
            if (XApp.sDebug) err.printStackTrace();
        }
        WorkAlive.scheduleTask(XApp.app);
        return Result.success();
    }
}
