package com.bhex.wallet.market.ui.activity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.market.R;
import com.bhex.wallet.market.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gongdongyang
 * 2020-8-31 16:37:06
 * 兑币页面
 */
@Route(path = ARouterConfig.Market_exchange_coin, name = "兑币")
public class ExchangeCoinActivity extends BaseActivity {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_coin;
    }

    @Override
    protected void initView() {
        tv_center_title.setText(getString(R.string.exchange_coin));
    }

    @Override
    protected void addEvent() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}