package com.bhex.wallet.app;

import com.bhex.network.app.BaseApplication;
import com.bhex.wallet.common.config.BHFilePath;

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
        //BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }
}
