package com.bhex.wallet.mnemonic.ui.activity;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.network.mvx.base.BaseActivity;
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
public class ExportKeyStoreActivity extends BaseActivity {

    @Autowired(name="title")
    String title;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_export_key_store;
    }

    @Override
    protected void initView() {
        tv_center_title.setText(title);
    }

    @Override
    protected void addEvent() {

    }
}
