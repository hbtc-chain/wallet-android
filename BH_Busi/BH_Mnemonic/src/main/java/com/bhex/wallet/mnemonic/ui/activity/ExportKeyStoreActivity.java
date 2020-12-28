package com.bhex.wallet.mnemonic.ui.activity;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.GradientTabLayout;
import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 导出KeyStore
 * 2020-5-14 13:30:21
 */
@Route(path = ARouterConfig.TRUSTEESHIP_EXPORT_KEYSTORE)
public class ExportKeyStoreActivity extends ExportBaseActivity {

    @Autowired(name="title")
    String title;

    @Autowired(name="flag")
    String flag;

    @Autowired(name="inputPwd")
    String inputPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_export_key_store;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        super.initView();
        tv_center_title.setText(title);
    }

    @Override
    protected void addEvent() {

    }

    @Override
    protected String getFlag() {
        return flag;
    }

    @Override
    protected String getInputPwd() {
        return inputPwd;
    }
}
