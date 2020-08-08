package com.x.codeX.agent;

import com.x.codeX.XApp;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class AdInfo implements Serializable {
    private String platformType; //平台类型
    private String adSize;//广告类型
    private List<String> ids;
    private String id;

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getAdSize() {
        return adSize;
    }

    public void setAdSize(String adSize) {
        this.adSize = adSize;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
       /* if (XApp.sDebug) {
            if (platformType.equals("fan")) {
                return "IMG_16_9_LINK#YOUR_PLACEMENT_ID";
            } else {
                if (adSize.equals("interstitial")) {
                    return "24534e1901884e398f1253216226017e";
                } else {
                    if (adSize.equals("small")) {
                        return "b195f8dd8ded45fe847ad89ed1d016da";
                    } else {
                        return "252412d5e9364a05ab77d9396346d73d";
                    }

                }

            }
        } else {
            if (ids != null) {
                return ids.get(new Random().nextInt(ids.size()));
            } else return null;
        }*/
        if (ids != null && !ids.isEmpty()) {
            return ids.get(new Random().nextInt(ids.size()));
        } else return null;
    }
}
