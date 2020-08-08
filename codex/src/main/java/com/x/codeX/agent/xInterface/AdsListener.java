package com.x.codeX.agent.xInterface;

import android.util.Log;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.InterstitialAdListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;
import com.x.codeX.XApp;
import com.x.codeX.agent.utils.XUtils;

public class AdsListener {
    public static MoPubInterstitial.InterstitialAdListener mopubInAdlistener(AdInterface adInterface,boolean isOut) {
        return new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                XUtils.reportAdLoad("mopub","interstitial",isOut);
                adInterface.onInAdLoadSucceeded();
                if (XApp.sDebug)
                Log.d("MoPubInterstitial", "onInAdLoadSucceeded");

            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                adInterface.onAdError();
                if (XApp.sDebug)
                Log.d("MoPubInterstitial", "onAdError" + errorCode);
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial interstitial) {
                adInterface.onAdShowSucceeded();
                if (XApp.sDebug)
                Log.d("MoPubInterstitial", "onInAdShowSucceeded");
            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial interstitial) {
                XUtils.reportAdClick("mopub","interstitial",isOut);
            }


            @Override
            public void onInterstitialDismissed(MoPubInterstitial interstitial) {
                adInterface.onInAdClosed();
                if (XApp.sDebug)
                Log.d("MoPubInterstitial", "onInAdClosed");

            }
        };
    }

    public static MoPubView.BannerAdListener mopubBannerListener(AdInterface adInterface,boolean isOut,String size) {
        return new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(MoPubView banner) {
                adInterface.onAdLoadSucceeded();
                if (XApp.sDebug)
                Log.d("MoPubBanner", "onBannerLoaded");
            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                adInterface.onAdError();
                if (XApp.sDebug)
                Log.d("MoPubBanner", "onAdError " + errorCode);

            }

            @Override
            public void onBannerClicked(MoPubView banner) {
                XUtils.reportAdClick("mopub",size,isOut);
            }

            @Override
            public void onBannerExpanded(MoPubView banner) {

            }

            @Override
            public void onBannerCollapsed(MoPubView banner) {

            }
        };
    }

    public static InterstitialAdListener interstitialAdListener(AdInterface adInterface,boolean isOut) {
        return new AbstractAdListener() {
            public void onInterstitialDisplayed(Ad ad) {
                XUtils.reportAdLoad("fan","interstitial",isOut);
                adInterface.onAdShowSucceeded();
                if (XApp.sDebug)
                Log.d("FanInterstitial", "onInAdShowSucceeded");
            }

            public void onInterstitialDismissed(Ad ad) {
                adInterface.onInAdClosed();
                if (XApp.sDebug)
                Log.d("FanInterstitial", "onInAdClosed");

            }

            public void onError(Ad ad, AdError adError) {
                adInterface.onAdError();
                if (XApp.sDebug)
                Log.d("FanInterstitial", "onAdError");
            }

            public void onAdLoaded(Ad ad) {
                adInterface.onInAdLoadSucceeded();
                if (XApp.sDebug)
                Log.d("FanInterstitial", "onInAdLoadSucceeded");

            }

            public void onAdClicked(Ad ad) {
                XUtils.reportAdClick("fan","interstitial",isOut);
            }
        };
    }

    public static AdListener fanAdListener(AdInterface adInterface,boolean isOut,String size) {
        return new AdListener() {
            public void onLoggingImpression(Ad ad) {

            }

            public void onError(Ad ad, AdError adError) {
                adInterface.onAdError();
                if (XApp.sDebug)
                    Log.d("FanAd", "onAdError");

            }

            public void onAdLoaded(Ad ad) {
                adInterface.onAdLoadSucceeded();
                if (XApp.sDebug)
                Log.d("FanAd", "onInAdLoadSucceeded");

            }

            public void onAdClicked(Ad ad) {
                XUtils.reportAdClick("fan",size,isOut);
                if (XApp.sDebug)
                Log.d("FanAd", "onInClick");
            }
        };

    }

}
