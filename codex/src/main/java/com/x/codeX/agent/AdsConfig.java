package com.x.codeX.agent;

import android.text.TextUtils;

import com.flurry.android.FlurryConfig;
import com.x.codeX.XApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AdsConfig {
    private volatile static AdsConfig adsConfig;
    private static List<AdInfo> allAdinfos;


    public static AdsConfig getAdsConfig() {
        if (adsConfig == null) {
            synchronized (AdsConfig.class) {
                if (adsConfig == null) {
                    adsConfig = new AdsConfig();
                }
            }
        }
        return adsConfig;
    }


    public static List<AdInfo> getAdId(String adSize) {
        List<AdInfo> adInfos = new ArrayList<>();
        getAdInfos(adInfos, adSize, "fan");
        getAdInfos(adInfos, adSize, "mopub");
        return adInfos;
    }

    private static void getAdInfos(List<AdInfo> adInfos, String adSize, String adType) {
        if (allAdinfos != null && allAdinfos.size() > 0) {
            for (int i = 0; i < allAdinfos.size(); i++) {
                if (allAdinfos.get(i).getPlatformType().equals(adType)) {
                    if (allAdinfos.get(i).getAdSize().contains(adSize)) {
                        AdInfo adInfo = new AdInfo();
                        adInfo.setAdSize(allAdinfos.get(i).getAdSize());
                        adInfo.setPlatformType(allAdinfos.get(i).getPlatformType());
                        adInfo.setIds(allAdinfos.get(i).getIds());
                        adInfos.add(adInfo);
                    }
                }
            }
        }
    }

    private AdsConfig() {
        allAdinfos = new ArrayList<>();
        AdInfo fanSmallAd = new AdInfo();
        fanSmallAd.setAdSize("small");
        fanSmallAd.setPlatformType("fan");

        AdInfo fanMediumAd = new AdInfo();
        fanMediumAd.setAdSize("medium");
        fanMediumAd.setPlatformType("fan");

        AdInfo fanIntAd = new AdInfo();
        fanIntAd.setAdSize("interstitial");
        fanIntAd.setPlatformType("fan");

        AdInfo mopubIntAd = new AdInfo();
        mopubIntAd.setAdSize("interstitial");
        mopubIntAd.setPlatformType("mopub");

        AdInfo mopubSmallAd = new AdInfo();
        mopubSmallAd.setAdSize("small");
        mopubSmallAd.setPlatformType("mopub");


        AdInfo mopubMediumAd = new AdInfo();
        mopubMediumAd.setAdSize("medium");
        mopubMediumAd.setPlatformType("mopub");
        allAdinfos.add(fanSmallAd);
        allAdinfos.add(fanMediumAd);
        allAdinfos.add(fanIntAd);
        allAdinfos.add(mopubIntAd);
        allAdinfos.add(mopubSmallAd);
        allAdinfos.add(mopubMediumAd);
        String fanConfig = FlurryConfig.getInstance().getString("ads_fallback_fan", null);
        if (fanConfig != null) {
            try {
                JSONArray fanJson = new JSONArray(fanConfig);
                for (int i = 0; i < fanJson.length(); i++) {
                    JSONObject jsonObject = fanJson.optJSONObject(i);
                    if (jsonObject != null) {
                        JSONArray optString = jsonObject.optJSONArray("ids");
                        List<String> ids = new ArrayList<>();
                        if (optString != null && optString.length() > 0) {
                            for (int j = 0; j < optString.length(); j++) {
                                String id = optString.optString(j);
                                if (!TextUtils.isEmpty(id)) {
                                    ids.add(id.trim());
                                }
                            }
                        }
                        if (!ids.isEmpty()) {
                            String type = jsonObject.optString("type");
                            if ("banner".equalsIgnoreCase(type)) {
                                if ("small".equalsIgnoreCase(jsonObject.optString("size"))) {
                                    if (XApp.sDebug){
                                        List<String> debugIds = new ArrayList<>();
                                        debugIds.add("IMG_16_9_LINK#YOUR_PLACEMENT_ID");
                                        fanSmallAd.setIds(debugIds);
                                    }else {
                                        fanSmallAd.setIds(ids);
                                    }

                                } else {
                                    if (XApp.sDebug){
                                        List<String> debugIds = new ArrayList<>();
                                        debugIds.add("IMG_16_9_LINK#YOUR_PLACEMENT_ID");
                                        fanMediumAd.setIds(debugIds);
                                    }else {
                                        fanMediumAd.setIds(ids);
                                    }

                                }
                            } else if ("interstitial".equalsIgnoreCase(type)) {
                                if (XApp.sDebug){
                                    List<String> debugIds = new ArrayList<>();
                                    debugIds.add("IMG_16_9_LINK#YOUR_PLACEMENT_ID");
                                    fanIntAd.setIds(debugIds);
                                }else {
                                    fanIntAd.setIds(ids);
                                }

                            }
                        }
                    }
                }
            } catch (JSONException e) {
                if (XApp.sDebug) {
                    e.printStackTrace();
                }
            }
        }
        String MopubConfig = FlurryConfig.getInstance().getString("ads_fallback_mopub", null);
        if (MopubConfig != null) {
            try {
                JSONArray mopubJson = new JSONArray(MopubConfig);
                for (int i = 0; i < mopubJson.length(); i++) {
                    JSONObject jsonObject = mopubJson.optJSONObject(i);
                    if (jsonObject != null) {
                        JSONArray optString = jsonObject.optJSONArray("ids");
                        List<String> ids = new ArrayList<>();
                        if (optString != null && optString.length() > 0) {
                            for (int j = 0; j < optString.length(); j++) {
                                String id = optString.optString(j);
                                if (!TextUtils.isEmpty(id)) {
                                    ids.add(id.trim());
                                }
                            }
                        }
                        if (!ids.isEmpty()) {
                            String type = jsonObject.optString("type");
                            if ("interstitial".equalsIgnoreCase(type)) {
                                if (XApp.sDebug){
                                    List<String> debugIds = new ArrayList<>();
                                    debugIds.add("24534e1901884e398f1253216226017e");
                                    mopubIntAd.setIds(debugIds);
                                }else {
                                    mopubIntAd.setIds(ids);
                                }

                            } else if ("banner".equalsIgnoreCase(type)) {
                                if ("small".equalsIgnoreCase(jsonObject.optString("size"))) {
                                    if (XApp.sDebug){
                                        List<String> debugIds = new ArrayList<>();
                                        debugIds.add("b195f8dd8ded45fe847ad89ed1d016da");
                                        mopubSmallAd.setIds(debugIds);
                                    }else {
                                        mopubSmallAd.setIds(ids);
                                    }
                                } else {
                                    if (XApp.sDebug){
                                        List<String> debugIds = new ArrayList<>();
                                        debugIds.add("252412d5e9364a05ab77d9396346d73d");
                                        mopubMediumAd.setIds(debugIds);
                                    }else {
                                        mopubMediumAd.setIds(ids);
                                    }
                                }

                            }

                        }
                    }
                }
            } catch (JSONException e) {
                if (XApp.sDebug) {
                    e.printStackTrace();
                }
            }
        }
        MopubInit.getInstance(mopubMediumAd.getId());
    }

}
