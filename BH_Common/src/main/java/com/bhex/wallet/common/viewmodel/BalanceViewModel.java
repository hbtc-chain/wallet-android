package com.bhex.wallet.common.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.google.gson.JsonObject;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/4
 * Time: 0:04
 */
public class BalanceViewModel extends ViewModel {

    public static MutableLiveData<LoadDataModel<AccountInfo>> accountLiveData  = new MutableLiveData<>();

    //获取资产
    public void getAccountInfo(BaseActivity activity,String address){
       getAccountInfo(activity,address,false);
    }
    //获取资产
    public void getAccountInfo(BaseActivity activity,String address,boolean showDialog){
        BHProgressObserver<JsonObject> observer = new BHProgressObserver<JsonObject>(activity,showDialog) {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                //super.onSuccess(jsonObject);
                AccountInfo accountInfo = JsonUtils.fromJson(jsonObject.toString(),AccountInfo.class);
                LoadDataModel loadDataModel = new LoadDataModel(accountInfo);
                accountLiveData.postValue(loadDataModel);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel loadDataModel = new LoadDataModel(0,"");
                accountLiveData.postValue(loadDataModel);
            }
        };

        BHttpApi.getService(BHttpApiInterface.class)
                .loadAccount(BHUserManager.getInstance().getCurrentBhWallet().address)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }

}
