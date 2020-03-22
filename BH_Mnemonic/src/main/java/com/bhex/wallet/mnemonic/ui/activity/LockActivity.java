package com.bhex.wallet.mnemonic.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.bhex.lib.uikit.widget.InputView;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.utils.ARouterUtil;
import com.bhex.wallet.common.viewmodel.WalletViewModel;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.persenter.LoginPresenter;
import com.bhex.wallet.mnemonic.ui.fragment.AddressFragment;
import com.bhex.wallet.mnemonic.ui.item.BHWalletItem;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录验证界面
 *
 * @author gongdongyang
 * 2020-3-12
 */
public class LockActivity extends BaseActivity<LoginPresenter> implements AddressFragment.AddressChangeListener {

    @BindView(R2.id.tv_bh_address)
    AppCompatTextView tv_bh_address;

    @BindView(R2.id.inp_wallet_pwd)
    InputView inp_wallet_pwd;

    @BindView(R2.id.btn_confirm)
    AppCompatButton btn_confirm;

    @BindView(R2.id.tv_import_mnemonic)
    AppCompatTextView tv_import_mnemonic;

    @BindView(R2.id.tv_forget_pwd)
    AppCompatTextView tv_forget_pwd;
    @BindView(R2.id.btn_wallet_create)
    MaterialButton btn_wallet_create;
    @BindView(R2.id.btn_wallet_impot)
    MaterialButton btn_wallet_impot;

    //当前钱包
    private BHWallet mCurrentWallet;

    private WalletViewModel walletVM;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lock;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this);
    }

    @Override
    protected void initView() {
        mCurrentWallet = BHUserManager.getInstance().getCurrentBhWallet();
        tv_bh_address.setText(mCurrentWallet.getAddress());
        getPresenter().proccessAddress(tv_bh_address, mCurrentWallet.getAddress());

    }

    @Override
    protected void addEvent() {
        walletVM = ViewModelProviders.of(this).get(WalletViewModel.class);
        walletVM.mutableLiveData.observe(this, loadDataModel -> {
            if (loadDataModel.loadingStatus == LoadingStatus.SUCCESS) {
                //ToastUtils.showToast("==loadingStatus==" + loadDataModel.loadingStatus);
            }
        });
        inp_wallet_pwd.addTextWatch(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().setButtonStatus(btn_confirm, inp_wallet_pwd.getInputString());
            }
        });

    }

    @OnClick({R2.id.btn_confirm, R2.id.tv_import_mnemonic, R2.id.tv_forget_pwd, R2.id.tv_bh_address,
            R2.id.btn_wallet_create, R2.id.btn_wallet_impot})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_confirm) {
            getPresenter().verifyPassword(inp_wallet_pwd.getInputString(), mCurrentWallet);
        } else if (view.getId() == R.id.tv_import_mnemonic) {
            ARouterUtil.startActivity(ARouterConfig.TRUSTEESHIP_IMPORT_INDEX);
        } else if (view.getId() == R.id.tv_forget_pwd) {
            ARouterUtil.startActivity(ARouterConfig.TRUSTEESHIP_IMPORT_INDEX);
        } else if (view.getId() == R.id.tv_bh_address) {
            AddressFragment fragment = new AddressFragment();
            fragment.setChangeListener(this);
            fragment.show(getSupportFragmentManager(), "");
        } else if(view.getId() == R.id.btn_wallet_create){
            ARouterUtil.startActivity(ARouterConfig.TRUSTEESHIP_MNEMONIC_FRIST);
        } else if(view.getId() == R.id.btn_wallet_impot){
            ARouterUtil.startActivity(ARouterConfig.TRUSTEESHIP_IMPORT_INDEX);

        }
    }


    @Override
    public void onItemClick(int position) {
        BHWallet bhWallet = BHUserManager.getInstance().getAllWallet().get(position);
        if(bhWallet.id!=mCurrentWallet.id){
            mCurrentWallet = bhWallet;
            BHUserManager.getInstance().setCurrentBhWallet(mCurrentWallet);
            walletVM.updateWallet(this, mCurrentWallet, mCurrentWallet.id, BHWalletItem.SELECTED);
            getPresenter().proccessAddress(tv_bh_address, mCurrentWallet.getAddress());
        }

    }


}
