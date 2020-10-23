package com.bhex.lib.uikit.widget.recyclerview;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/5
 * Time: 17:27
 */
public class MyLinearLayoutManager extends LinearLayoutManager {

    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollHorizontally() {
        //return super.canScrollHorizontally();
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        //return super.canScrollVertically();
        return false;
    }
}
