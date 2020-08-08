package com.x.codeX.agent;

import android.content.Context;

import com.x.codeX.XApp;

import net.grandcentrix.tray.AppPreferences;

public class LocalStorageManager {
    private static volatile LocalStorageManager sInst = null;

    public static LocalStorageManager getInst() {
        if (sInst == null) {
            synchronized (LocalStorageManager.class) {
                if (sInst == null) {
                    sInst = new LocalStorageManager(XApp.app);
                }
            }
        }

        return sInst;
    }

    private final AppPreferences mPref;

    private LocalStorageManager(Context context) {
        mPref = new AppPreferences(context);
    }

    public long getLong(String key, long defVal) {
        return mPref.getLong(key, defVal);
    }

    public void updateLong(String key, long val) {
        mPref.put(key, val);
    }

    public boolean getBoolean(String key, boolean defVal) {
        return mPref.getBoolean(key, defVal);
    }

    public void updateBoolean(String key, boolean val) {
        mPref.put(key, val);
    }

    public String getString(String key, String defVal) {
        return mPref.getString(key, defVal);
    }

    public void updateString(String key, String val) {
        mPref.put(key, val);
    }
}
