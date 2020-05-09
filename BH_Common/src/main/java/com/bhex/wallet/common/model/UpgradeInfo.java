package com.bhex.wallet.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author gongdongyang
 * 2020年5月8日22:01:29
 * 版本升级
 */
public class UpgradeInfo {

    @SerializedName("download_url")
    public String downloadUrl;

    @SerializedName("ndownload_webview_url")
    public String downloadWebviewUrl;

    @SerializedName("need_force_update")
    public boolean needForceUpdate;

    @SerializedName("need_update")
    public boolean needUpdate;

    @SerializedName("new_features")
    public String newFeatures;

    public String apkVersion;

}
