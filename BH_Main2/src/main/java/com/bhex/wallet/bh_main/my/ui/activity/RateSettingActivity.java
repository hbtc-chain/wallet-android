package com.bhex.wallet.bh_main.my.ui.activity;


import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.common.config.ARouterConfig;

/**
 * @author gongdongyang
 * 2020-4-18 16:13:56
 * 汇率设置
 */
@Route(path = ARouterConfig.MY_Rate_setting)
public class RateSettingActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rate_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addEvent() {

    }
}
