package com.bhex.wallet.app;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;


import com.bhex.lib.uikit.util.TypefaceUtils;
import com.bhex.network.app.BaseApplication;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.wallet.common.manager.MMKVManager;

import java.util.concurrent.TimeUnit;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class BHApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemConfig.getInstance().init();
        TypefaceUtils.replaceSystemDefaultFont(BHApplication.getInstance());
        //夜间模式
        AppCompatDelegate.setDefaultNightMode(MMKVManager.getInstance().getSelectNightMode());
        //rateSync();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalManageUtil.attachBaseContext(base,""));
    }

    /*private void rateSync(){
        PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(RateSyncWork.class,5L, TimeUnit.MILLISECONDS);
        WorkManager.getInstance(this).enqueue(builder.build());
    }*/
}
