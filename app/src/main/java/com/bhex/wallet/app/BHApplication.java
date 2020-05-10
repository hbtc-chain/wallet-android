package com.bhex.wallet.app;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.webkit.CookieManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.bhex.lib.uikit.util.TypefaceUtils;
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
        TypefaceUtils.replaceSystemDefaultFont(this);
        //夜间模式
        AppCompatDelegate.setDefaultNightMode(MMKVManager.getInstance().getSelectNightMode());

        BHFilePath.initPath(this);

        //BlockCanary.install(this, new AppBlockCanaryContext()).start();

    }

    /*@Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources != null && (resources.getConfiguration()).fontScale != 1F) {
            Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1F;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }*/



}
