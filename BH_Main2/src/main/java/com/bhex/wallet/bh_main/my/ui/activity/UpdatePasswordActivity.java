package com.bhex.wallet.bh_main.my.ui.activity;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.InputView;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.presenter.MyPresenter;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.config.ARouterUrls;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.viewmodel.WalletViewModel;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-3-21 21:18:44
 * 修改密码
 */

@Route(path= ARouterConfig.MY_UPDATE_PASSWORD)
public class UpdatePasswordActivity extends BaseActivity<MyPresenter>{
    @Autowired(name="title")
    String title;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.inp_old_pwd)
    InputView inp_old_pwd;
    @BindView(R2.id.inp_new_pwd)
    InputView inp_new_pwd;
    @BindView(R2.id.inp_confrim_pwd)
    InputView inp_confrim_pwd;

    @BindView(R2.id.btn_next)
    MaterialButton btn_next;

    @BindView(R2.id.pwd_tips_0)
    AppCompatTextView pwd_tips_0;

    @BindView(R2.id.pwd_tips_1)
    AppCompatTextView pwd_tips_1;

    @BindView(R2.id.pwd_tips_2)
    AppCompatTextView pwd_tips_2;

    @BindView(R2.id.pwd_tips_3)
    AppCompatTextView pwd_tips_3;

    @BindView(R2.id.pwd_tips_4)
    AppCompatTextView pwd_tips_4;

    private WalletViewModel walletViewModel;

    private BHWallet mCurrentWallet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_password;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        tv_center_title.setText(title);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MyPresenter(this);
    }

    @Override
    protected void addEvent() {
        mCurrentWallet = BHUserManager.getInstance().getCurrentBhWallet();
        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel.class);
        walletViewModel.walletLiveData.observe(this,loadDataModel -> {
            if(loadDataModel.loadingStatus== LoadingStatus.SUCCESS){
                ToastUtils.showToast("密码修改成功");
                finish();
            }else{
                ToastUtils.showToast("密码修改失败");
            }
        });

        inp_old_pwd.addTextWatch(passwordTextWatcher);

        inp_new_pwd.addTextWatch(passwordTextWatcher);

        inp_confrim_pwd.addTextWatch(passwordTextWatcher);
    }


    @OnClick({R2.id.btn_next})
    public void onViewClicked(View view) {
        if(view.getId() == R.id.btn_next){
            boolean flag = getPresenter().checkPasswordEqual(
                    inp_old_pwd.getInputString().trim(),
                    inp_new_pwd.getInputString().trim(),
                    inp_confrim_pwd.getInputString().trim());
            if(flag){
                BHWallet item = getPresenter().makeBhWallet(mCurrentWallet);
                walletViewModel.updatePassword(this,inp_new_pwd.getInputString(),item);
            }
        }
    }

    private SimpleTextWatcher passwordTextWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            String oldPwd = inp_old_pwd.getInputString();
            String newPwd = inp_new_pwd.getInputString();
            String newConfrimPwd = inp_confrim_pwd.getInputString();
            mPresenter.checkPasswordIsInput(btn_next,oldPwd,newPwd,newConfrimPwd,pwd_tips_0,pwd_tips_1,pwd_tips_2,pwd_tips_3,pwd_tips_4);
        }
    };
}
