package com.bhex.wallet.common.manager;

import com.bhex.network.mvx.base.BaseActivity;

/**
 * @author gongdongyang
 * 2020-10-12 17:22:39
 */
public class MainActivityManager {

    public static volatile  MainActivityManager _instance = new MainActivityManager();

    public  BaseActivity mainActivity;
    private MainActivityManager(){

    }

}
