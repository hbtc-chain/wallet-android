package com.bhex.wallet.bh_main.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.TypefaceUtils;
import com.bhex.network.RxSchedulersHelper;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NavigateUtil;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.R;
import com.bhex.wallet.app.BHApplication;
import com.bhex.wallet.common.config.ARouterConfig;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        //hideBottomUIMenu();

        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel.class);
        walletViewModel.loadWallet(this);

        //boolean flag = MMKVManager.getInstance().mmkv().decodeBool(BHConstants.FRIST_BOOT);
        //
        Disposable disposable = Observable.just(0).timer(1000, TimeUnit.MILLISECONDS)
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(aLong -> {
                    //首次启动
                    if (!BHUserManager.getInstance().isHasWallet()) {
                        NavigateUtil.startActivity(SplashActivity.this, MnemonicIndexActivity.class);
                    } else {
                        boolean isFinger = MMKVManager.getInstance().mmkv().decodeBool(BHConstants.FINGER_PWD_KEY);
                        if (!isFinger) {
                            //NavigateUtil.startActivity(SplashActivity.this, MainActivity.class);
                            ARouter.getInstance().build(ARouterConfig.Main.main_mainindex).navigation();
                        } else {
                            //NavigateUtil.startActivity(SplashActivity.this, FingerLoginActivity.class);
                            ARouter.getInstance().build(ARouterConfig.Account.Account_Login_Finger).navigation();
                        }

                    }
                    finish();
                });

        mCompositeDisposable.add(disposable);

        ImmersionBar.with(this)
                .statusBarColor(android.R.color.white)
                .statusBarDarkFont(true)
                .navigationBarDarkIcon(true)
                .navigationBarColor(R.color.white)
                .fullScreen(true)
                .init();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.attachBaseContext(newBase, ""));
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

    private void dispose() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
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


    /*protected void hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }else if (Build.VERSION.SDK_INT >= 19) {
            Window _window = getWindow();
            WindowManager.LayoutParams params = _window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
            _window.setAttributes(params);
        }

    }

    public void test(){
        DecimalFormat df = new DecimalFormat();
        df.setGroupingUsed(false);
        df.setRoundingMode(RoundingMode.DOWN);
        df.setMaximumFractionDigits(5);
        String  result = df.format(74.99);
        System.out.println(result);
        LogUtils.e("SplashActivity===>","result=="+result);
        String  result0 = NumberUtil.formatValue(74.99,5);
        LogUtils.e("SplashActivity===>","result0=="+result0);

    }*/
}