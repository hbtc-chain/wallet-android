package com.bhex.wallet.mnemonic.ui.activity;

import android.text.Editable;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.lib.uikit.widget.InputView;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.persenter.LoginPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录验证界面
 *
 * @author gongdongyang
 * 2020-3-12
 */
public class LoginActivity extends BaseActivity<LoginPresenter> {

    @BindView(R2.id.tv_bh_address)
    AppCompatTextView tv_bh_address;

    @BindView(R2.id.inp_wallet_pwd)
    InputView inp_wallet_pwd;

    @BindView(R2.id.btn_confirm)
    AppCompatButton btn_confirm;


    //当前钱包
    private BHWallet mCurrentWallet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this);
    }

    @Override
    protected void initView() {
        mCurrentWallet = BHUserManager.getInstance().getBhWallet();
        tv_bh_address.setText(mCurrentWallet.getAddress());
        getPresenter().proccessAddress(tv_bh_address, mCurrentWallet.getAddress());

    }

    @Override
    protected void addEvent() {
        inp_wallet_pwd.addTextWatch(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().setButtonStatus(btn_confirm, inp_wallet_pwd.getInputString());
            }
        });

    }

    @OnClick({R2.id.btn_confirm})
    public void onViewClicked(View view) {

        if(view.getId()==R.id.btn_confirm){
            getPresenter().verifyPassword(inp_wallet_pwd.getInputString(),mCurrentWallet);
        }
    }
}
