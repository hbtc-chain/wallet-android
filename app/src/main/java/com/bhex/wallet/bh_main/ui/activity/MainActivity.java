package com.bhex.wallet.bh_main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.RefreshLayoutManager;
import com.bhex.lib.uikit.util.BottomNavigationViewUtil;
import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.R;
import com.bhex.wallet.bh_main.persenter.MainPresenter;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.enums.TRANSCATION_BUSI_TYPE;
import com.bhex.wallet.common.event.AccountEvent;
import com.bhex.wallet.common.event.LanguageEvent;
import com.bhex.wallet.common.event.NightEvent;
import com.bhex.wallet.common.manager.AppStatusManager;
import com.bhex.wallet.common.manager.MainActivityManager;
import com.bhex.wallet.common.viewmodel.BalanceViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * created by gongdongyang
 * on 2020/2/24
 */

@Route(path= ARouterConfig.Main.main_mainindex)
public class MainActivity extends BaseActivity<MainPresenter> {

    @BindView(R.id.main_bottom)
    BottomNavigationView mBottomNavigationView;

    private long mExitTime = 0L;
    private int mCurrentCheckId = 0;
    //是否复位
    public static boolean isReset = true;

    private BalanceViewModel balanceViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        MainActivityManager._instance.mainActivity = this;
        RefreshLayoutManager.init();
        TRANSCATION_BUSI_TYPE.init(this);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //LogUtils.d("MainPresenter===>:","isReset=2="+isReset);

        if(savedInstanceState!=null && !isReset){
            mCurrentCheckId = savedInstanceState.getInt("index",0);
        }
        mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(mCurrentCheckId).getItemId());
    }

    @Override
    protected void addEvent() {
        balanceViewModel = ViewModelProviders.of(MainActivity.this).get(BalanceViewModel.class).build(MainActivity.this);
        getLifecycle().addObserver(balanceViewModel);

        EventBus.getDefault().register(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.tab_balance:
                    mCurrentCheckId = 0;
                    getPresenter().goBalanceFragment();
                    return true;
                case R.id.tab_validator:
                    mCurrentCheckId = 1;
                    getPresenter().goMarketFragment();
                    return true;
                /*case R.id.tab_proposals:
                    mCurrentCheckId = 2;
                    getPresenter().goProposalFragment();
                    return true;*/
                case R.id.tab_my:
                    mCurrentCheckId = 2;
                    getPresenter().goMyFragment();
                    return true;
            }
            return false;
        });
        mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(0).getItemId());
        BottomNavigationViewUtil.hideToast(mBottomNavigationView);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenter(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(System.currentTimeMillis()-this.mExitTime>2000L){
            ToastUtils.showToast(getString(R.string.exit_app));
            this.mExitTime = System.currentTimeMillis();
            return;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void changeLanguage(LanguageEvent language){
        isReset = false;
        recreate();
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void changeAccount(AccountEvent language){
        isReset = true;
        mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(0).getItemId());
        getPresenter().showIsBackup();
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void changeAccount(NightEvent nightEvent){
        isReset = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(AppStatusManager.KEY_HOME_ACTION, AppStatusManager.ACTION_BACK_TO_HOME);
        if(action==AppStatusManager.ACTION_RESTART_APP){
            ARouter.getInstance().build(ARouterConfig.Main.main_splash).navigation();
            finish();
        }
        String go_position = intent.getStringExtra("go_position");
        String go_token  = intent.getStringExtra("go_token");
        getPresenter().gotoTarget(mBottomNavigationView,go_position,go_token);
    }

    @Override
    protected int getStatusColorValue() {
        return BHConstants.STATUS_COLOR_TRANS;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index",mCurrentCheckId);
    }



}
