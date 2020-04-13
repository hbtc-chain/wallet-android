package com.bhex.wallet.market.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.base.viewmodel.AutoDisposeViewModel;
import com.bhex.network.observer.BaseObserver;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.market.api.MarketApiInterface;
import com.bhex.wallet.market.api.MarketNetworkApi;
import com.bhex.wallet.market.model.MarketAllEntity;


import java.util.List;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class MarketListViewModel extends AutoDisposeViewModel {

    public MutableLiveData<List<MarketAllEntity.MarketItemEntity>> marketDatas= new MutableLiveData();

    //public MutableLiveData<LoadingStatus> statusMutableLiveData = new MutableLiveData();

    public MutableLiveData<LoadDataModel<MarketAllEntity>> marketLiveDatas = new MutableLiveData();

    public void loadMarketAllData(){
        BaseObserver<MarketAllEntity> observer = new BaseObserver<MarketAllEntity>() {
            @Override
            public void onSuccess(MarketAllEntity marketAllEntity) {
                marketLiveDatas.postValue(new LoadDataModel<MarketAllEntity>(marketAllEntity));
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                marketLiveDatas.postValue(new LoadDataModel(code,errorMsg));
            }
        };



        MarketNetworkApi.getService(MarketApiInterface.class)
                .getMarketAllData()
                .compose(RxSchedulersHelper.<MarketAllEntity>io_main())
                .compose(MarketNetworkApi.getInstance().<MarketAllEntity>applySchedulersMap())
                //.compose(MarketNetworkApi.getInstance().<MarketAllEntity>applySchedulersError())
                .subscribe(observer);

    }
}
