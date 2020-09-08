package com.bhex.wallet.bh_main.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.bhex.lib.uikit.util.TypefaceUtils;
import com.bhex.network.RxSchedulersHelper;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NavigateUtil;
import com.bhex.wallet.R;
import com.bhex.wallet.app.BHApplication;
import com.bhex.wallet.common.config.BHFilePath;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.common.utils.BHKey;
import com.bhex.wallet.common.viewmodel.WalletViewModel;
import com.bhex.wallet.mnemonic.MnemonicIndexActivity;
import com.bhex.wallet.mnemonic.ui.activity.FingerLoginActivity;
import com.bhex.wallet.mnemonic.ui.activity.LockActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 *  启动页
 * @author
 */
public class SplashActivity extends AppCompatActivity {

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private WalletViewModel walletViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);

        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel.class);
        walletViewModel.loadWallet(this);

        boolean flag = MMKVManager.getInstance().mmkv().decodeBool(BHConstants.FRIST_BOOT);
        //
        Disposable disposable = Observable.just(0).timer(1000, TimeUnit.MILLISECONDS)
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(aLong -> {
                    //首次启动
                    LogUtils.d("SplashActivity===>:","aLong=="+aLong);
                    if(!BHUserManager.getInstance().isHasWallet()){
                        NavigateUtil.startActivity(SplashActivity.this, MnemonicIndexActivity.class);
                    }else {
                        boolean isFinger = MMKVManager.getInstance().mmkv().decodeBool(BHConstants.FINGER_PWD_KEY);
                        if(!isFinger){
                            NavigateUtil.startActivity(SplashActivity.this, LockActivity.class);
                        }else{
                            NavigateUtil.startActivity(SplashActivity.this, FingerLoginActivity.class);
                        }
                    }
                    finish();
                });

        mCompositeDisposable.add(disposable);

        ImmersionBar.with(this).statusBarColor(android.R.color.white).statusBarDarkFont(true).init();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.attachBaseContext(newBase,""));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }

    private void dispose(){
        if(mCompositeDisposable!=null &&!mCompositeDisposable.isDisposed()){
            mCompositeDisposable.dispose();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dispose();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BHUserManager.getInstance();
        BHFilePath.initPath(BHApplication.getInstance());
    }
}
