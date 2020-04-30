package com.bhex.wallet.app;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.webkit.CookieManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.bhex.network.app.BaseApplication;
import com.bhex.wallet.common.config.BHFilePath;
import com.bhex.wallet.common.manager.MMKVManager;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class BHApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemConfig.getInstance().init();
        //夜间模式
        AppCompatDelegate.setDefaultNightMode(MMKVManager.getInstance().getSelectNightMode());

        //AppFilePath.init(this);
        BHFilePath.initPath(this);
        //BlockCanary.install(this, new AppBlockCanaryContext()).start();

    }

    /*@Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }*/
}
