package com.bhex.wallet.common.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.IPresenter;
import com.bhex.wallet.common.ActivityCache;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/13
 * Time: 17:45
 */
public abstract class BaseCacheActivity<T extends IPresenter> extends BaseActivity<T> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCache.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCache.getInstance().removeActivity(this);
    }
}
