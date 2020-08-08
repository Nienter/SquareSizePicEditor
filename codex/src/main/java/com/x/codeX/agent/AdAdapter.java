package com.x.codeX.agent;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;
import com.x.codeX.R;
import com.x.codeX.XApp;
import com.x.codeX.agent.xInterface.AdInterface;
import com.x.codeX.agent.xInterface.AdsListener;

import static com.mopub.mobileads.MoPubView.MoPubAdSize.HEIGHT_250;
import static com.mopub.mobileads.MoPubView.MoPubAdSize.HEIGHT_50;

public class AdAdapter {
    private String id;
    private Context context;

    private AdView fanAdView;
    private InterstitialAd fanInAdView;
    private AdInterface adInterface;
    private MoPubView moPubView;
    private MoPubInterstitial moPubInterstitial;
    private Adtype adtype;
    private LinearLayout mopubAdLayout;
    private boolean isOut;//是否为外展

    public void init(Context context, Adtype adtype, String id,boolean isOut) {
        this.context = context;
        this.id = id;
        this.adtype = adtype;
        this.isOut = isOut;
    }

    public void setAdInterface(AdInterface adInterface) {
        this.adInterface = adInterface;
    }

    public void load() {
        if (XApp.sDebug) {
            Log.d(adtype.name(), "onAdload");
        }
        switch (adtype) {
            case FAN_IN:
                fanInAdView = new InterstitialAd(context, id);
                fanInAdView.setAdListener(AdsListener.interstitialAdListener(adInterface,isOut));
                fanInAdView.loadAd();
                break;
            case FAN_SMALL:
                fanAdView = new AdView(context, id, AdSize.BANNER_HEIGHT_50);
                fanAdView.setAdListener(AdsListener.fanAdListener(adInterface,isOut,"small"));
                fanAdView.loadAd();
                break;
            case FAN_MEDIUM:
                fanAdView = new AdView(context, id, AdSize.RECTANGLE_HEIGHT_250);
                fanAdView.setAdListener(AdsListener.fanAdListener(adInterface,isOut,"medium"));
                fanAdView.loadAd();
                break;
            case MOPUB_IN:
                if (!MopubInit.isInit) {
                    MopubInit.getInstance(id);
                }

                if (moPubInterstitial ==null) {
                    moPubInterstitial = new MoPubInterstitial((Activity) context, id);
                    moPubInterstitial.setInterstitialAdListener(AdsListener.mopubInAdlistener(adInterface, isOut));
                    moPubInterstitial.load();
                }
                break;
            case MOPUB_SMALL:
                if (!MopubInit.isInit) {
                    MopubInit.getInstance(id);
                }
                if (mopubAdLayout==null) {
                    mopubAdLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.mopub_banner, null, false);
                    moPubView = mopubAdLayout.findViewById(R.id.mopub_adview);
                    moPubView.setAdUnitId(id);
                    moPubView.setAdSize(HEIGHT_50);
                    moPubView.setBannerAdListener(AdsListener.mopubBannerListener(adInterface,isOut,"small"));
                    moPubView.loadAd();
                }
                break;
            case MOPUB_MEDIUM:
                if (!MopubInit.isInit) {
                    MopubInit.getInstance(id);
                }
                if (mopubAdLayout==null) {
                    mopubAdLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.mopub_banner, null, false);
                    moPubView = mopubAdLayout.findViewById(R.id.mopub_adview);
                    moPubView.setAdUnitId(id);
                    moPubView.setAdSize(HEIGHT_250);
                    moPubView.setBannerAdListener(AdsListener.mopubBannerListener(adInterface,isOut,"medium"));
                    moPubView.loadAd();
                }

                break;
        }
    }

    public View getView() {
        switch (adtype) {
            case FAN_SMALL:
            case FAN_MEDIUM:
                return fanAdView;
            case MOPUB_SMALL:
            case MOPUB_MEDIUM:
                return mopubAdLayout;
            default:
                return null;
        }
    }

    public void showInAd() {
        switch (adtype) {
            case MOPUB_IN:
                moPubInterstitial.show();
                break;
            case FAN_IN:
                fanInAdView.show();
                break;
        }
    }


    public void adDestory() {
        if (moPubInterstitial != null) {
            moPubInterstitial.destroy();
            if (XApp.sDebug)
            Log.d("moPubInterstitial", "destory");
        }
        if (moPubView != null) {
            moPubView.destroy();
            if (XApp.sDebug)
            Log.d("moPubView", "destory");

        }
        if (fanInAdView != null) {
            fanInAdView.destroy();
            if (XApp.sDebug)
            Log.d("fanInAdView", "destory");

        }
        if (fanAdView != null) {
            fanAdView.destroy();
            if (XApp.sDebug)
            Log.d("fanAdView", "destory");
        }
    }
}
