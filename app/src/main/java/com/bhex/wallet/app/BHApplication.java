package com.bhex.wallet.app;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.bhex.lib.uikit.util.TypefaceUtils;
import com.bhex.network.app.BaseApplication;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.tools.toast.BHToastStyle;
import com.bhex.wallet.common.config.BHFilePath;
import com.bhex.wallet.common.manager.MMKVManager;
import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastBlackStyle;

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
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalManageUtil.attachBaseContext(base,""));
    }


}
