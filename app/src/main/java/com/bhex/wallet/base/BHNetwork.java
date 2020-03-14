package com.bhex.wallet.base;

import android.app.Application;

import com.bhex.network.BuildConfig;
import com.bhex.network.base.INetworkRequiredInfo;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class BHNetwork implements INetworkRequiredInfo {

    private Application mApplication;

    public BHNetwork(Application application) {
        this.mApplication = application;
    }

    @Override
    public String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getAppVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public Application getApplicationContext() {
        return mApplication;
    }
}
