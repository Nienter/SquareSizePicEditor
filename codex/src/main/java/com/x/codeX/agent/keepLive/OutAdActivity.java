package com.x.codeX.agent.keepLive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.flurry.android.FlurryConfig;
import com.x.codeX.R;
import com.x.codeX.XApp;
import com.x.codeX.agent.AdAdapter;
import com.x.codeX.agent.AdInfo;
import com.x.codeX.agent.AdsConfig;
import com.x.codeX.agent.Adtype;
import com.x.codeX.agent.utils.SpUtil;
import com.x.codeX.agent.utils.XUtils;
import com.x.codeX.agent.xInterface.AdInterface;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class OutAdActivity extends AppCompatActivity {
    private AdAdapter outFullAdapter;
//    private AdAdapter outNativeAdapter;
    private static Iterator<AdInfo> outInfoIterator;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_out_ad);
        progressBar = findViewById(R.id.progress_bar);

        loadFullAd(true);
        //isFirstLoad  是否可以加载其他大小的AD
/*        if (new Random().nextInt(100) > FlurryConfig.getInstance().getInt("bergInner_pr", 20)) {
            loadFullAd(true);
        } else {
            loadNativeAd(true);
        }*/


    }

    private void loadFullAd(boolean isFirstLoad) {
        outFullAdapter = new AdAdapter();
        List<AdInfo> adInfos = AdsConfig.getAdId("interstitial");
        if (adInfos.size() > 0) {
            outInfoIterator = adInfos.iterator();
            loadNext(outFullAdapter, outInfoIterator, "interstitial", isFirstLoad);
        } else {
          /*  if (isFirstLoad) {
                loadNativeAd(false);
            } else*/
                finish();
        }
    }

/*
    private void loadNativeAd(boolean isFirstLoad) {
        outNativeAdapter = new AdAdapter();
        List<AdInfo> adInfos = AdsConfig.getAdId("medium");
        if (adInfos.size() > 0) {
            outInfoIterator = adInfos.iterator();
            loadNext(outNativeAdapter, outInfoIterator, "medium", isFirstLoad);
        } else {
            if (isFirstLoad) {
                loadFullAd(false);
            } else
                finish();
        }
    }
*/

    private void loadNext(AdAdapter adapter, Iterator<AdInfo> inInfoIterator, String adSize, boolean isFirstType) {
        if (inInfoIterator.hasNext()) {
            AdInfo adInfo = inInfoIterator.next();
            if (XUtils.getOutTime(adInfo.getPlatformType()) > 0 && adInfo.getId() != null) {
                adapter.init(this, Adtype.getType(adInfo), adInfo.getId(), true);
                AdInterface adInterface = new AdInterface() {
                    public void onInAdClosed() {
                        super.onInAdClosed();
                        destory(adapter);
                        finish();
                    }

                    @Override
                    public void onAdError() {
                        super.onAdError();
                        destory(adapter);
                        loadNext(adapter, inInfoIterator, adSize, isFirstType);
                    }

                 /*   @Override
                    public void onAdLoadSucceeded() {
                        super.onAdLoadSucceeded();
                        progressBar.setVisibility(View.GONE);
                        findViewById(R.id.iv_delete).setVisibility(View.VISIBLE);
                        findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                destory(adapter);
                                finish();
                            }
                        });
                        View bannerView = outNativeAdapter.getView();
                        if (bannerView != null) {
                            FrameLayout frameLayout = new FrameLayout(XApp.app);
                            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            if (((ViewGroup) findViewById(R.id.native_ad_holder)).getChildCount() == 0) {
                                frameLayout.addView(bannerView);
                                ((ViewGroup) findViewById(R.id.native_ad_holder)).removeAllViews();
                                ((ViewGroup) findViewById(R.id.native_ad_holder)).addView(frameLayout);
                                XUtils.reportAdLoad(adInfo.getPlatformType(), "medium", true);
                            }
                        }
                    }*/

                    @Override
                    public void onInAdLoadSucceeded() {
                        super.onInAdLoadSucceeded();
                        progressBar.setVisibility(View.GONE);
                        outFullAdapter.showInAd();
                    }

                    @Override
                    public void onAdShowSucceeded() {
                        super.onAdShowSucceeded();
                        int time = SpUtil.mPrefSp().getInt(adInfo.getPlatformType() + "_open_time", 0);
                        SpUtil.mPrefSp().put(adInfo.getPlatformType() + "_open_time", time + 1);
                    }
                };
                adapter.setAdInterface(adInterface);
                adapter.load();
            } else {
                loadNext(adapter, inInfoIterator, adSize, isFirstType);
            }

        } else {
            destory(adapter);
            finish();
    /*        if (isFirstType) {
                if (adSize.equals("interstitial")) {
                    loadNativeAd(false);
                } else {
                    loadFullAd(false);
                }
            } else {
                finish();
            }*/
        }
    }

    private void destory(AdAdapter adapter) {
        adapter.adDestory();
    }

}
