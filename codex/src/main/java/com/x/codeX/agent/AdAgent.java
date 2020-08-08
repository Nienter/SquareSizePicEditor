package com.x.codeX.agent;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Keep;
import androidx.fragment.app.FragmentTransaction;

import com.flurry.android.FlurryConfig;
import com.x.codeX.XApp;
import com.x.codeX.agent.utils.XUtils;
import com.x.codeX.agent.xInterface.AdInterface;
import com.x.codeX.agent.xInterface.DInterface;

import java.util.Iterator;
import java.util.List;

public class AdAgent {
    public final Context context;
    private final String adSize;//small,native
    private final ViewGroup viewGroup;
    public final Boolean isShowBannerAd;//内部ad控制
    @Keep
    public Boolean isShowInAd;//插屏控制  false防止hook startActivity在跳转mopub插屏时的循环问题
    private static Iterator<AdInfo> bannerInfoIterator;
    private static Iterator<AdInfo> inInfoIterator;
    private AdAdapter adAdapter;
    private AdAdapter inAdAdapter;
    @Keep
    private static DInterface dInterface;
    private Boolean isInAdReady = false;
    private Boolean isShowFromFragment = false;
    private FragmentTransaction fragmentTransaction;


    private AdAgent(Builder builder) {
        this.context = builder.context;
        this.adSize = builder.adSize;
        this.viewGroup = builder.viewGroup;
        this.isShowBannerAd = builder.isShowInAd;
        this.isShowInAd = builder.isShowOutAd;
    }

    public static class Builder {
        private final Context context;
        private String adSize;
        private ViewGroup viewGroup;
        private Boolean isShowInAd = true;
        private Boolean isShowOutAd = true;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setAdInfo(String adSize, ViewGroup viewGroup) {
            this.adSize = adSize;
            this.viewGroup = viewGroup;

            return this;
        }

        public Builder isShowInAd(Boolean isShowInAd) {
            this.isShowInAd = isShowInAd;
            return this;
        }

        public Builder isShowOutAd(Boolean isShowOutAd) {
            this.isShowOutAd = isShowOutAd;
            return this;
        }

        public AdAgent build() {
            return new AdAgent(this);
        }
    }


    public void loadAd() {
        if(com.flurry.sdk.a.i()) {
            if (XUtils.isInappAdsEnabled()) {
                if (isShowBannerAd && viewGroup != null) {
                    adAdapter = new AdAdapter();
                    List<AdInfo> bannerInfos = AdsConfig.getAdId(adSize);
                    if (bannerInfos.size() > 0) {
                        bannerInfoIterator = bannerInfos.iterator();
                        loadNext(adAdapter, bannerInfoIterator);
                    }
                }
                if (isShowInAd) {
                    loadInAd();
                }
            } else {
                isShowInAd = false;
            }
        }
    }

    private void loadInAd() {
        inAdAdapter = new AdAdapter();
        List<AdInfo> InInfos = AdsConfig.getAdId("interstitial");
        if (InInfos.size() > 0) {
            inInfoIterator = InInfos.iterator();
            loadNext(inAdAdapter, inInfoIterator);
        }

    }

    public void showAdForFragment(FragmentTransaction fragmentTransaction) {
        if (isInAdReady) {
            this.fragmentTransaction = fragmentTransaction;
            isShowFromFragment = true;
            inAdAdapter.showInAd();
        } else {
            fragmentTransaction.commitAllowingStateLoss();
        }

    }


    public void showInAD() {
        if (isInAdReady) {
            isShowInAd = false;
            destory(adAdapter);
            inAdAdapter.showInAd();
        }
    }

    private void loadNext(AdAdapter adapter, Iterator<AdInfo> inInfoIterator) {
        if (inInfoIterator.hasNext()) {
            AdInfo adInfo = inInfoIterator.next();
            if (adInfo.getId() != null) {
                adapter.init(context, Adtype.getType(adInfo), adInfo.getId(), false);
                AdInterface adInterface = new AdInterface() {
                    public void onInAdClosed() {
                        super.onAdError();
                        if (isShowFromFragment) {
                            destory(inAdAdapter);
                            isInAdReady = false;
                            isShowFromFragment = false;
                            fragmentTransaction.commitAllowingStateLoss();
                            loadInAd();
                        } else {
                            destory(adAdapter);
                            destory(inAdAdapter);
                            isShowInAd = false;
                            if (dInterface != null) {
                                dInterface.geNext();
                            }
                        }
                    }

                    @Override
                    public void onAdError() {
                        super.onAdError();
                        destory(adapter);
                        loadNext(adapter, inInfoIterator);
                    }

                    @Override
                    public void onInAdLoadSucceeded() {
                        super.onInAdLoadSucceeded();
                        isInAdReady = true;
                    }

                    @Override
                    public void onAdLoadSucceeded() {
                        super.onAdLoadSucceeded();
                        View bannerView = adapter.getView();
                        if (bannerView != null) {
                            FrameLayout frameLayout = new FrameLayout(XApp.app);
                            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            if (viewGroup.getChildCount() == 0) {
                                frameLayout.addView(bannerView);
                                viewGroup.removeAllViews();
                                viewGroup.addView(frameLayout);
                                XUtils.reportAdLoad(adInfo.getPlatformType(), adInfo.getAdSize(), false);
                            }
                        }
                    }
                };
                adapter.setAdInterface(adInterface);
                adapter.load();
            } else {
                loadNext(adapter, inInfoIterator);
            }
        } else {
            isShowInAd = false;
            destory(adapter);
        }
    }

    @Keep
    public void showIn() {
        if (isInAdReady) {
            isShowInAd = false;
            destory(adAdapter);
            inAdAdapter.showInAd();
        } else {
            isShowInAd = false;
            destory(inAdAdapter);
            if (dInterface != null) {
                dInterface.geNext();
            }
        }
    }

    private void destory(AdAdapter adapter) {
        if (adapter != null)
            adapter.adDestory();
    }
}
