package com.x.codeX.agent.keepLive;

import android.content.Context;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.x.codeX.XApp;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WorkAlive {
    public static void init(Context context) {
        scheduleTask(context);
        startRouse(context);
    }


    public static void scheduleTask(Context context) {
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(OutAdWorker.class)
                .setConstraints(new Constraints.Builder().build())
                .setInitialDelay(XApp.sDebug ? 50 : new Random().nextInt(50)+100, TimeUnit.SECONDS)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).enqueueUniqueWork(XApp.getAppName() + "bgwork", ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);
    }

    public static void startRouse(Context context) {
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest
                .Builder(RouseWorker.class, new Random().nextInt(5)+16, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(XApp.getAppName() + "rousework", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }
}
