package com.bhex.network.utils;

import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.StringRes;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class ToastUtils {

    public static void showToast(@StringRes int resId){
        com.hjq.toast.ToastUtils.show(resId);
    }

    public static void showToast(String message){
        if(TextUtils.isEmpty(message)){
            return;
        }

        if(Thread.currentThread()!= Looper.getMainLooper().getThread()){
            return;
        }
        com.hjq.toast.ToastUtils.show(message);
    }


}
