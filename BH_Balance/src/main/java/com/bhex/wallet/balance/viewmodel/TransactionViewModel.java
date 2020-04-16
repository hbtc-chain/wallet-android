package com.bhex.wallet.balance.viewmodel;

import android.app.Activity;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.utils.HUtils;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.api.TransactionApi;
import com.bhex.wallet.common.api.TransactionApiInterface;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.TransactionOrder;
import com.google.gson.JsonObject;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/1
 * Time: 15:23
 */
public class TransactionViewModel extends ViewModel {

    public MutableLiveData<LoadDataModel> mutableLiveData  = new MutableLiveData<>();
    public MutableLiveData<LoadDataModel<List<TransactionOrder>>> transLiveData  = new MutableLiveData<>();
    /**
     *
     * @param activity
     * @param bhSendTranscation
     */
    public void sendTransaction(BaseActivity activity, final BHSendTranscation bhSendTranscation){
        String body = JsonUtils.toJson(bhSendTranscation);
        LogUtils.d("TransactionViewModel==>:","body=="+ JsonUtils.toJson(bhSendTranscation));
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
                LoadDataModel lmd = new LoadDataModel(LoadingStatus.ERROR);
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


    /**
     * 查询所有交易记录
     */
    public void queryTransctionByAddress(BaseActivity activity,String address,int page,String token,String type){

        BHBaseObserver<JsonObject> observer = new BHBaseObserver<JsonObject>() {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                //super.onSuccess(jsonObject);
                String txs = JsonUtils.getAsJsonArray(jsonObject.toString(),"txs").toString();

                if(!TextUtils.isEmpty(txs)){
                    List<TransactionOrder> list = JsonUtils.getListFromJson(txs, TransactionOrder.class);
                    LoadDataModel ldm = new LoadDataModel(list);
                    transLiveData.postValue(ldm);
                }else{
                    LoadDataModel ldm = new LoadDataModel(LoadingStatus.ERROR,"");
                    transLiveData.postValue(ldm);
                }



            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel ldm = new LoadDataModel(LoadingStatus.ERROR,"");
                transLiveData.postValue(ldm);
            }
        };
        BHttpApi.getService(BHttpApiInterface.class)
                .queryTransctionByAddress(address,page, BHConstants.PAGE_SIZE,token,type)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }


}
