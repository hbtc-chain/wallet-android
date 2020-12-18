package com.bhex.wallet.common.base;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.bhex.network.mvx.base.IPresenter;

import org.jetbrains.annotations.NotNull;

/**
 * created by gongdongyang
 * on 2020/2/26
 */
public abstract class BasePresenter implements IPresenter {

    protected BaseActivity mBaseActivity;

    public BasePresenter(BaseActivity activity) {
        this.mBaseActivity = activity;
    }

    public BaseActivity getActivity() {
        return mBaseActivity;
    }


    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {

    }

    @Override
    public void onStart(@NotNull LifecycleOwner owner) {

    }

    @Override
    public void onPuase(@NotNull LifecycleOwner owner) {

    }

    @Override
    public void onStop(@NotNull LifecycleOwner owner) {

    }

    @Override
    public void onResume(@NotNull LifecycleOwner owner) {

    }

    @Override
    public void onDestroy(@NotNull LifecycleOwner owner) {

    }

    @Override
    public void onLifecycleChanged(@NotNull LifecycleOwner owner, @NotNull Lifecycle.Event event) {

    }
}
