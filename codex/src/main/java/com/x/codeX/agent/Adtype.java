package com.x.codeX.agent;

public enum Adtype {
    FAN_SMALL, FAN_MEDIUM, FAN_IN, MOPUB_SMALL, MOPUB_MEDIUM, MOPUB_IN;

    public static Adtype getType(AdInfo adInfo) {

        if (adInfo.getPlatformType().equals("fan")){
            if (adInfo.getAdSize().equals("small")){
                return FAN_SMALL;
            }else if (adInfo.getAdSize().equals("medium")){
                return FAN_MEDIUM;
            }else {
                return FAN_IN;
            }
        }else {
            if (adInfo.getAdSize().equals("small")){
                return MOPUB_SMALL;
            }else if (adInfo.getAdSize().equals("medium")){
                return MOPUB_MEDIUM;
            }else {
                return MOPUB_IN;
            }
        }

    }
}
