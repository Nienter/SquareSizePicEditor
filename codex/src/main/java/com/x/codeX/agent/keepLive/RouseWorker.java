package com.x.codeX.agent.keepLive;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.x.codeX.XApp;
import com.x.codeX.agent.FlurryUtils;
import com.x.codeX.agent.utils.XUtils;


public class RouseWorker extends Worker {
    public RouseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            if(XApp.sDebug){
                Log.d("RouseWorker", "tick");
            }
            XUtils.checkAppClude();
            FlurryUtils.Fetch();
        } catch (Exception err) {
            if (XApp.sDebug) err.printStackTrace();
        }

        WorkAlive.scheduleTask(XApp.app);
        
        return Result.success();
    }
}
