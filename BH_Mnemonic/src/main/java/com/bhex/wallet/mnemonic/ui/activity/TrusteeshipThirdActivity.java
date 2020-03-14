package com.bhex.wallet.mnemonic.ui.activity;

import android.text.Editable;
import android.view.View;
import android.widget.CheckedTextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bhex.lib.uikit.widget.InputView;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.Constants;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.persenter.TrusteeshipPresenter;
import com.bhex.wallet.mnemonic.viewmodel.WalletViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 创建托管单元第二步
 * 2020-3-12 20:47:54
 */
public class TrusteeshipThirdActivity extends BaseCacheActivity<TrusteeshipPresenter> {

    WalletViewModel walletViewModel;

    @BindView(R2.id.inp_wallet_confirm_pwd)
    InputView inp_wallet_confirm_pwd;

    @BindView(R2.id.btn_create)
    AppCompatButton btn_create;

    @BindView(R2.id.ck_agreement)
    CheckedTextView ck_agreement;

    @BindView(R2.id.tv_password_count)
    AppCompatTextView tv_password_count;

    String mOldPwd;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_trusteeship_third;
    }

    @Override
    protected void initView() {
        mOldPwd = BHUserManager.getInstance().getBhWallet().getPassword();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TrusteeshipPresenter(this);
    }

    @Override
    protected void addEvent() {
        //获取输入密码
        inp_wallet_confirm_pwd.addTextWatch(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                mPresenter.checkConfirmPassword(inp_wallet_confirm_pwd,btn_create,mOldPwd,ck_agreement);

                //
                int count = inp_wallet_confirm_pwd.getInputString().length();
                tv_password_count.setText(String.format(getString(R.string.pwd_index),count));
            }
        });

        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel.class);
        //walletViewModel.setmContext(this);


    }


    @OnClick({R2.id.btn_create,R2.id.ck_agreement})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_create){
            //设置密码
            /*BHUserManager.getInstance().getBhWallet().setPassword(inp_wallet_pwd.getInputString());
            NavitateUtil.startActivity(this,TrusteeshipThirdActivity.class);*/
            String userName = BHUserManager.getInstance().getBhWallet().getName();
            generateMnemonic(mOldPwd,userName);
        }else if(view.getId()==R.id.ck_agreement){

            ck_agreement.setChecked(!ck_agreement.isChecked());

            getPresenter().checkConfirmPassword(inp_wallet_confirm_pwd,btn_create,mOldPwd,ck_agreement);
        }
    }

    /**
     * 生成助记词
     */
    private void generateMnemonic(String name, String pwd) {
        walletViewModel.generateMnemonic(this,name, pwd);

        walletViewModel.mutableLiveData.observe(this,status -> {
            if ("1".equals(status)) {
                NavitateUtil.startActivity(TrusteeshipThirdActivity.this, TrusteeshipSuccessActivity.class);
                MMKVManager.getInstance().mmkv().encode(Constants.FRIST_BOOT, true);
            } else {
                ToastUtils.showToast("创建失败");
            }
        });
    }

}
