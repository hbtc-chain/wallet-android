package com.bhex.wallet.bh_main.my.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.common.config.ARouterConfig;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-5-16 16:14:15
 * 消息中心
 */
@Route(path = ARouterConfig.MY_Message,name="消息中心")
public class MessageActivity extends BaseActivity {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {
        tv_center_title.setText(getResources().getString(R.string.message_center));
    }

    @Override
    protected void addEvent() {

    }
}
