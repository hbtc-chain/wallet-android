package com.bhex.wallet.balance.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.base.viewmodel.CacheAndroidViewModel;
import com.bhex.network.cache.RxCache;
import com.bhex.network.cache.data.CacheResult;
import com.bhex.network.cache.stategy.CacheStrategy;
import com.bhex.network.cache.stategy.IStrategy;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.receiver.NetWorkStatusChangeReceiver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.wallet.balance.model.BHTokenItem;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.model.BHPage;
import com.bhex.wallet.common.model.BHToken;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/3
 * Time: 15:31
 */
public class CoinViewModel extends CacheAndroidViewModel {

    public CoinViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<LoadDataModel> coinLiveData  = new MutableLiveData<>();

    /**
     * 请求所有币种
     */
    public void loadCoin(BaseActivity activity){

        BHBaseObserver<JsonObject> observer = new BHBaseObserver<JsonObject>() {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                Type type = new TypeToken<BHPage<BHTokenItem>>() {}.getType();
                BHPage<BHTokenItem> page = JsonUtils.fromJson(jsonObject.toString(),type);
                LoadDataModel loadDataModel = new LoadDataModel(page);
                coinLiveData.postValue(loadDataModel);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel ldm = new LoadDataModel(LoadingStatus.ERROR,errorMsg);
                coinLiveData.postValue(ldm);
            }
        };
        Type type = (new TypeToken<JsonObject>() {}).getType();

        BHttpApi.getService(BHttpApiInterface.class)
                .loadSymbol(1,2000)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().<JsonObject>transformObservable(SymbolCache.CACHE_KEY, type, getCacheStrategy()))
                .map(new CacheResult.MapFunc<JsonObject>())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);

    }


}
