package com.bhex.wallet.common.utils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.wallet.common.manager.BHUserManager;

/**
 * created by gongdongyang
 * on 2020/3/4
 */
public class ARouterUtil {


    public static void startActivity(String path){
        ARouter.getInstance().build(path).navigation();
    }

    public static void startActivityTarget(String path,Class target){
        ARouter.getInstance().build(path).navigation();
        BHUserManager.getInstance().setTargetClass(target);
    }
}
