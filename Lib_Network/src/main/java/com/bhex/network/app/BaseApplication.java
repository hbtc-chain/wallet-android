package com.bhex.network.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;

import com.hjq.toast.ToastUtils;

import java.util.concurrent.Executor;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static  BaseApplication _instance;

    private static Handler sHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
        _instance = this;
        sHandler = new Handler(_instance.getMainLooper());

    }

    public static BaseApplication getInstance() {
        return _instance;
    }

    public static Handler getMainHandler() {
        return sHandler;
    }


    /**
     * 所有的新建线程，不要直接new thread，使用此线程池来做执行
     *
     * @return
     */
    public Executor getExecutor() {
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }

}
