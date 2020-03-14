package com.bhex.wallet.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.BuildConfig;

/**
 * created by gongdongyang
 * on 2020/2/26
 */
public class AppBlockCanaryContext extends BlockCanaryContext {

    private static final String TAG = "AppContext";

    @Override
    public String provideQualifier() {
        String qualifier = "";
        try {
            PackageInfo info = BHApplication.getInstance().getPackageManager()
                    .getPackageInfo(BHApplication.getInstance().getPackageName(), 0);
            qualifier += info.versionCode + "_" + info.versionName + "_YYB";
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "provideQualifier exception", e);
        }
        return qualifier;
    }

    @Override
    public int provideBlockThreshold() {
        return 500;
    }

    @Override
    public boolean displayNotification() {
        return true;
    }

    @Override
    public boolean stopWhenDebugging() {
        return false;
    }
}
