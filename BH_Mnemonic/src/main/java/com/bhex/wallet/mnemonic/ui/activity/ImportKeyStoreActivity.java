package com.bhex.wallet.mnemonic.ui.activity;


import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.lib.uikit.widget.InputView;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BaseCoreActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.common.ActivityCache;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.MAKE_WALLET_TYPE;
import com.bhex.wallet.common.event.AccountEvent;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.utils.BHWalletUtils;
import com.bhex.wallet.common.viewmodel.WalletViewModel;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * Keystore导入
 * 2020-5-14 12:16:43
 */
@Route(path = ARouterConfig.TRUSTEESHIP_IMPORT_KEYSTORE)
public class ImportKeyStoreActivity extends BaseCacheActivity {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.et_keystore)
    AppCompatEditText et_keystore;

    @BindView(R2.id.inp_origin_pwd)
    InputView inp_origin_pwd;

    @BindView(R2.id.btn_next)
    AppCompatTextView btn_next;

    WalletViewModel walletViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_key_store;
    }

    @Override
    protected void initView() {
        tv_center_title.setText(getResources().getString(R.string.import_keystore));
        et_keystore.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et_keystore.setGravity(Gravity.TOP);
        et_keystore.setSingleLine(false);
        et_keystore.setHorizontallyScrolling(false);
    }

    @Override
    protected void addEvent() {
        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel.class);
        walletViewModel.mutableLiveData.observe(this,ldm->{
            importKeyStoreStatus(ldm);
        });
    }

    @OnClick({R2.id.btn_next})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_next){
            importKeyStore();
        }

    }

    /**
     * 导入KeyStore
     */
    private void importKeyStore() {
        String keyStoreStr = et_keystore.getText().toString().trim();
        String password = inp_origin_pwd.getInputString().trim();
        String name= "gongdongyang";
        if(TextUtils.isEmpty(keyStoreStr)){
            ToastUtils.showToast("请输入KeyStore");
            return ;
        }

        if(TextUtils.isEmpty(password)){
            ToastUtils.showToast(getResources().getString(R.string.please_input_password));
            return ;
        }

        BHUserManager.getInstance().getTmpBhWallet().setWay(MAKE_WALLET_TYPE.导入KS.getWay());
        walletViewModel.importKeyStore(this,keyStoreStr,name,password);
    }

    /**
     * 导入KeyStore后的回调
     */
    private void importKeyStoreStatus(LoadDataModel<BHWallet> ldm){
        if(ldm.getLoadingStatus()== LoadingStatus.SUCCESS){
            if(ldm.getData().id==0){
                ToastUtils.showToast(getResources().getString(R.string.trusteeship_exist));
            }else{
                NavitateUtil.startMainActivity(this,new String[]{});
                ActivityCache.getInstance().finishActivity();
                EventBus.getDefault().post(new AccountEvent());
                ToastUtils.showToast(getResources().getString(R.string.import_keystore_success));
            }

        }else if(ldm.getLoadingStatus()== LoadingStatus.ERROR){
            ToastUtils.showToast(ldm.msg);
        }
    }

}
