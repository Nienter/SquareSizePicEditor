package com.x.codeX.agent;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.x.codeX.XApp;

public class MopubInit {
    private volatile static MopubInit mopubInit;
    public static boolean isInit = false;

    public static MopubInit getInstance(String id) {
        if (mopubInit == null) {
            synchronized (MopubInit.class) {
                if (mopubInit == null) {
                    if (id != null)
                        mopubInit = new MopubInit(id);
                }
            }
        }
        return mopubInit;
    }

    private MopubInit(String id) {
        if (!isInit) {
            SdkConfiguration configuration = new SdkConfiguration.Builder(id)
                    .build();
            MoPub.initializeSdk(XApp.app, configuration, new SdkInitializationListener() {
                @Override
                public void onInitializationFinished() {
                    isInit = MoPub.isSdkInitialized();
                }
            });
        }
    }
}
