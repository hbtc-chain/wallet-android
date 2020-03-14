package com.bhex.network.base;

import android.app.Application;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public interface INetworkRequiredInfo {
    String getAppVersionName();
    String getAppVersionCode();
    boolean isDebug();
    Application getApplicationContext();
}
