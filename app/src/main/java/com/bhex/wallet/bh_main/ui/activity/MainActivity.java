package com.bhex.wallet.bh_main.ui.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.RefreshLayoutManager;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.R;
import com.bhex.wallet.balance.enums.TRANSCATION_BUSI_TYPE;
import com.bhex.wallet.bh_main.persenter.MainPresenter;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.event.LanguageEvent;
import com.bhex.wallet.common.event.ThemeEvent;
import com.bhex.wallet.common.manager.MMKVManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * created by gongdongyang
 * on 2020/2/24
 */

@Route(path= ARouterConfig.APP_MAIN_PAGE)
public class MainActivity extends BaseActivity<MainPresenter> {

    @BindView(R.id.main_bottom)
    BottomNavigationView mBottomNavigationView;

    private long mExitTime = 0L;

    private int mCurrentCheckId = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        RefreshLayoutManager.init();
        TRANSCATION_BUSI_TYPE.init(this);
        //透明状态栏
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {}
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
        //getLifecycle().addObserver(getPresenter());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(MMKVManager.getInstance().getSelectNightMode());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.d("MainActivity===>","==onRestoreInstanceState==");
        if(savedInstanceState!=null){
            mCurrentCheckId = savedInstanceState.getInt("index",0);
            mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(mCurrentCheckId).getItemId());
        }
    }

    @Override
    protected void addEvent() {

        EventBus.getDefault().register(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.tab_balance:
                    mCurrentCheckId = 0;
                    getPresenter().goBalanceFragment();
                    return true;
                case R.id.tab_validator:
                    mCurrentCheckId = 1;
                    getPresenter().goValidatorFragment();
                    return true;
                case R.id.tab_proposals:
                    mCurrentCheckId = 2;
                    getPresenter().goProposalFragment();
                    return true;
                case R.id.tab_my:
                    mCurrentCheckId = 3;
                    getPresenter().goMyFragment();
                    return true;
            }
            return false;
        });

        mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(0).getItemId());
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
        recreate();
    }

    /*@Subscribe(threadMode= ThreadMode.MAIN)
    public void changeTheme(ThemeEvent event){

    }*/

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected int getStatusColorValue() {
        return BHConstants.STATUS_COLOR_TRANS;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //LogUtils.d("MainActivity===>","==outState=="+outState);
        outState.putInt("index",mCurrentCheckId);
    }
}
