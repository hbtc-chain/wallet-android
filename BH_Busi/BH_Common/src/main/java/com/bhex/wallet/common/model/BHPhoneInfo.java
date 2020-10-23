package com.bhex.wallet.common.model;

import android.app.Application;
import android.content.Context;

import com.bhex.network.utils.PackageUtils;

/**
 * 设备信息
 */
public class BHPhoneInfo {
    //appId
    public static String appId;
    //appVersion
    public static String appVersion;
    //
    public static String deviceType = "android";
    //
    public static String deviceVersion;


    //初始化设备信息
    public static void initPhoneInfo(Application application){
        appVersion = PackageUtils.getVersionName(application);
        appId = PackageUtils.getApplicationId(application);
        deviceVersion = android.os.Build.VERSION.RELEASE;
        deviceType = "android";
    }
}
