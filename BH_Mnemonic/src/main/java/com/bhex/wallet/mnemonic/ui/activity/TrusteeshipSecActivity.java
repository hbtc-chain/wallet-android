package com.bhex.wallet.mnemonic.ui.activity;

import android.text.Editable;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.lib.uikit.widget.InputView;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.persenter.TrusteeshipPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 创建托管单元第二步
 * 2020-3-12 20:47:54
 */
public class TrusteeshipSecActivity extends BaseCacheActivity<TrusteeshipPresenter> {

    @BindView(R2.id.inp_wallet_pwd)
    InputView inp_wallet_pwd;

    @BindView(R2.id.btn_next)
    AppCompatButton btn_next;

    @BindView(R2.id.tv_password_count)
    AppCompatTextView tv_password_count;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trusteeship_sec;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initPresenter() {
        mPresenter = new TrusteeshipPresenter(this);

    }

    @Override
    protected void addEvent() {
        inp_wallet_pwd.addTextWatch(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                mPresenter.checkPassword(inp_wallet_pwd, btn_next);
                int count = inp_wallet_pwd.getInputString().length();
                tv_password_count.setText(String.format(getString(R.string.pwd_index),count));
            }
        });


    }


    @OnClick({R2.id.btn_next})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_next){
            //设置密码
            BHUserManager.getInstance().getBhWallet().setPassword(inp_wallet_pwd.getInputString());
            NavitateUtil.startActivity(this,TrusteeshipThirdActivity.class);
        }
    }
}
