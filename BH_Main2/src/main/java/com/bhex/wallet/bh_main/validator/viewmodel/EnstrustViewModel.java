package com.bhex.wallet.bh_main.validator.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.utils.HUtils;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.api.TransactionApi;
import com.bhex.wallet.common.api.TransactionApiInterface;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.ValidatorDelegationInfo;
import com.bhex.wallet.common.model.ValidatorInfo;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.TransactionOrder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.List;

import okhttp3.RequestBody;

/**
 * Created by BHEX.
 * User: zc
 * Date: 2020/4/16
 */
public class EnstrustViewModel extends ViewModel {

    //public static MutableLiveData<LoadDataModel<AccountInfo>> accountLiveData  = new MutableLiveData<>();
    public static MutableLiveData<LoadDataModel<List<ValidatorDelegationInfo>>> delegationLiveData  = new MutableLiveData<>();
    public MutableLiveData<LoadDataModel> mutableLiveData  = new MutableLiveData<>();

    //获取资产
    /*public void getAccountInfo(BaseActivity activity){
        BHProgressObserver<JsonObject> observer = new BHProgressObserver<JsonObject>(activity) {
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
                LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                accountLiveData.postValue(loadDataModel);
            }
        };

        BHttpApi.getService(BHttpApiInterface.class)
                .loadAccount(BHUserManager.getInstance().getCurrentBhWallet().address)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }*/

    public void getCustDelegations(BaseActivity activity){
        BHProgressObserver<JsonArray> observer = new BHProgressObserver<JsonArray>(activity) {
            @Override
            protected void onSuccess(JsonArray jsonObject) {
                //super.onSuccess(jsonObject);
                List<ValidatorDelegationInfo> infos = JsonUtils.getListFromJson(jsonObject.toString(),ValidatorDelegationInfo.class);
                LoadDataModel loadDataModel = new LoadDataModel(infos);
                delegationLiveData.postValue(loadDataModel);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                delegationLiveData.postValue(loadDataModel);
            }
        };

        BHttpApi.getService(BHttpApiInterface.class)
                .queryCustDelegations(BHUserManager.getInstance().getCurrentBhWallet().address)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }
    /**
     *
     * @param activity
     * @param bhSendTranscation
     */
    public void sendDoEntrust(BaseActivity activity, final BHSendTranscation bhSendTranscation){
        String body = JsonUtils.toJson(bhSendTranscation);
        LogUtils.d("EnstrustViewModel==>:","body=="+ JsonUtils.toJson(bhSendTranscation));
        BHProgressObserver<JsonObject> observer = new BHProgressObserver<JsonObject>(activity) {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                super.onSuccess(jsonObject);
                LoadDataModel lmd = new LoadDataModel(LoadingStatus.SUCCESS);
                mutableLiveData.postValue(lmd);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                //LoadDataModel lmd = new LoadDataModel(LoadingStatus.ERROR);
                LoadDataModel lmd = new LoadDataModel(code,errorMsg);
                mutableLiveData.postValue(lmd);
            }
        };

        RequestBody txBody = HUtils.createJson(body);
        TransactionApi.getService(TransactionApiInterface.class)
                .sendTransaction(txBody)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }

}
