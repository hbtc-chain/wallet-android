package com.bhex.wallet.market.ui.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.market.R;

/**
 * @author gongdongyang
 * 2020-12-12 11:18:47
 */
@Route(path = ARouterConfig.Market.market_webview)
public class WebViewActivity extends BaseActivity {

    @Autowired(name = "url")
    public String url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fl_content, (Fragment) ARouter.getInstance()
                .build(ARouterConfig.Market.market_fragment_webview)
                .withString("url",url)
                .navigation());
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void addEvent() {

    }

}