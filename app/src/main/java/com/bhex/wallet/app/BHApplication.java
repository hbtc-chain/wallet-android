package com.bhex.wallet.app;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


import com.bhex.lib.uikit.util.TypefaceUtils;
import com.bhex.network.app.BaseApplication;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.common.work.RateSyncWork;

import java.util.concurrent.TimeUnit;

import xcrash.XCrash;

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
        XCrash.init(this);
    }

    private void rateSync(){
        PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(RateSyncWork.class,15L,TimeUnit.SECONDS);
        WorkManager.getInstance().enqueue(builder.build());
    }
}
