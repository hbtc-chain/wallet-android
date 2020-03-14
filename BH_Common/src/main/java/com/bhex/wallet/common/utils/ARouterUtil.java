package com.bhex.wallet.common.utils;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * created by gongdongyang
 * on 2020/3/4
 */
public class ARouterUtil {


    public static void startActivity(String path){
        ARouter.getInstance().build(path).navigation();
    }
}
