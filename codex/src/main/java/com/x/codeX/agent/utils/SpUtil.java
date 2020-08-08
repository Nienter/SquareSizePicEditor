package com.x.codeX.agent.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.x.codeX.XApp;

import java.util.Map;

public class SpUtil {
    private static final String Pref_FILE = XApp.app.getPackageName()+"_pref";
    private static SPImp spImp = new SPImp();

    public static SPImp mPrefSp() {
        return spImp.getSPImp(Pref_FILE, Context.MODE_PRIVATE);
    }

    public static class SPImp {
        private SharedPreferences sp;

        SPImp getSPImp(String name, int mode) {
            sp = XApp.app.getSharedPreferences(name, mode);
            return this;
        }

        public synchronized void put(String key, Object object) {
            synchronized (sp) {
                SharedPreferences.Editor editor = sp.edit();

                if (object instanceof String) {
                    editor.putString(key, (String) object);
                } else if (object instanceof Integer) {
                    editor.putInt(key, (Integer) object);
                } else if (object instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) object);
                } else if (object instanceof Float) {
                    editor.putFloat(key, (Float) object);
                } else if (object instanceof Long) {
                    editor.putLong(key, (Long) object);
                } else {
                    editor.putString(key, object.toString());
                }
                editor.apply();
            }
        }


        public String getString(String key, String defaultObject) {
            return sp.getString(key, defaultObject);
        }

        public Integer getInt(String key, Integer defaultObject) {
            return sp.getInt(key, defaultObject);
        }

        public Boolean getBoolean(String key, Boolean defaultObject) {
            return sp.getBoolean(key, defaultObject);
        }

        public Float getFloat(String key, Float defaultObject) {
            return sp.getFloat(key, defaultObject);
        }

        public Long getLong(String key, Long defaultObject) {
            return sp.getLong(key, defaultObject);
        }


    }
}
