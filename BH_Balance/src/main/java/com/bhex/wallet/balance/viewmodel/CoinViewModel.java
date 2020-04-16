package com.bhex.wallet.balance.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.wallet.balance.model.BHTokenItem;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.google.gson.JsonObject;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/3
 * Time: 15:31
 */
public class CoinViewModel extends ViewModel {

    public MutableLiveData<LoadDataModel<List<BHTokenItem>>> coinLiveData  = new MutableLiveData<>();

    /**
     * 请求所有币种
     */
    public void loadCoin(BaseActivity activity){

        BHProgressObserver<JsonObject> observer = new BHProgressObserver<JsonObject>(activity) {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                super.onSuccess(jsonObject);
                
                if(JsonUtils.isHasMember(jsonObject,"tokens")){
                    List<BHTokenItem> coinList = JsonUtils.getListFromJson(jsonObject.toString(),
                            "tokens", BHTokenItem.class);

                    LoadDataModel loadDataModel = new LoadDataModel(coinList);
                    coinLiveData.postValue(loadDataModel);
                }else{
                    LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                    coinLiveData.postValue(loadDataModel);
                }

            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);

                LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                coinLiveData.postValue(loadDataModel);
            }
        };

        BHttpApi.getService(BHttpApiInterface.class)
                .loadSymbol(1,2000)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);

    }
}
