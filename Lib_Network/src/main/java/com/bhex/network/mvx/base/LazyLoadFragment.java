package com.bhex.network.mvx.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Created by gongdongyang
 * on 2019/12/13
 */
public abstract  class LazyLoadFragment extends BaseFragment {

    /**
     * 是否初始化过布局
     */
    protected boolean isViewInitiated;

    /**
     * 当前界面是否可见
     */

    protected boolean isVisibleToUser;
    /**
     * 是否加载过数据
     */
    protected boolean isDataInitiated;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            prepareFetchData();
        }
    }



    /**
     * 懒加载
     */
    public abstract void fetchData();

    public void prepareFetchData() {
        prepareFetchData(false);
    }

    /**
     * 判断懒加载条件
     *
     * @param forceUpdate 强制更新，好像没什么用？
     */
    public void prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
        }
    }

}
