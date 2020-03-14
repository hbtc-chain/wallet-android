package com.bhex.wallet.app;

import com.bhex.network.app.BaseApplication;
import com.bhex.network.base.NetworkApi;
import com.bhex.wallet.base.BHNetwork;
import com.bhex.wallet.common.config.AppFilePath;
import com.bhex.wallet.common.config.BHFilePath;
import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.tencent.mmkv.MMKV;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class BHApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemConfig.getInstance().init();
        //AppFilePath.init(this);
        BHFilePath.initPath(this);
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }
}
