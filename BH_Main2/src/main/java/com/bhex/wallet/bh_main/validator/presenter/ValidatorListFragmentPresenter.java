package com.bhex.wallet.bh_main.validator.presenter;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.cache.RxCache;
import com.bhex.network.cache.data.CacheResult;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.model.ValidatorInfo;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ValidatorListFragmentPresenter extends BasePresenter {

    public ValidatorListFragmentPresenter(BaseActivity activity) {
        super(activity);
    }

}
