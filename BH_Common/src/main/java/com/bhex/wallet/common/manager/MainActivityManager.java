package com.bhex.wallet.common.manager;

import androidx.lifecycle.ViewModelProviders;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.wallet.common.viewmodel.BalanceViewModel;

/**
 * @author gongdongyang
 * 2020-10-12 17:22:39
 */
public class MainActivityManager {

    public static volatile  MainActivityManager _instance = new MainActivityManager();

    public  BaseActivity mainActivity;

    private Class targetClass;

    private MainActivityManager(){

    }

    public static MainActivityManager getInstance(){
        return _instance;
    }

    public void stopAssetRequest(){
        if(mainActivity==null){
            return;
        }
        BalanceViewModel balanceViewModel = ViewModelProviders.of(mainActivity).get(BalanceViewModel.class).build(mainActivity);

        balanceViewModel.onDestroy();
    }

    public void startAssetRequest(){
        if(mainActivity==null){
            return;
        }
        BalanceViewModel balanceViewModel = ViewModelProviders.of(mainActivity).get(BalanceViewModel.class).build(mainActivity);

        balanceViewModel.onCreate();
    }


    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }
}
