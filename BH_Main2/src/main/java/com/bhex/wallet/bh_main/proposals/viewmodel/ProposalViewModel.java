package com.bhex.wallet.bh_main.proposals.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.observer.BaseObserver;
import com.bhex.network.utils.HUtils;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.api.TransactionApi;
import com.bhex.wallet.common.api.TransactionApiInterface;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.ProposalInfo;
import com.bhex.wallet.common.model.ProposalQueryResult;
import com.bhex.wallet.common.model.ValidatorInfo;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.TransactionOrder;
import com.bhex.wallet.common.utils.LiveDataBus;
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
public class ProposalViewModel extends ViewModel {

    public MutableLiveData<LoadDataModel<ProposalQueryResult>> proposalLiveData = new MutableLiveData<>();
    public MutableLiveData<LoadDataModel<ProposalInfo>> proposalInfoLiveData = new MutableLiveData<>();
    public MutableLiveData<LoadDataModel> doPledgeLiveData  = new MutableLiveData<>();
    public MutableLiveData<LoadDataModel> doVetoLiveData  = new MutableLiveData<>();
    public MutableLiveData<LoadDataModel> createProposalLiveData  = new MutableLiveData<>();
    //获取资产
    public void getAccountInfo(BaseActivity activity,boolean isShowDialog){
        BHProgressObserver<JsonObject> observer = new BHProgressObserver<JsonObject>(activity,isShowDialog) {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                //super.onSuccess(jsonObject);
                AccountInfo accountInfo = JsonUtils.fromJson(jsonObject.toString(),AccountInfo.class);
                LoadDataModel loadDataModel = new LoadDataModel(accountInfo);
                //accountLiveData.postValue(loadDataModel);
                LiveDataBus.getInstance().with(BHConstants.Label_Account,LoadDataModel.class).postValue(loadDataModel);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel loadDataModel = new LoadDataModel(code,"");
                //accountLiveData.postValue(loadDataModel);
                LiveDataBus.getInstance().with(BHConstants.Label_Account,LoadDataModel.class).postValue(loadDataModel);

            }
        };

        BHttpApi.getService(BHttpApiInterface.class)
                .loadAccount(BHUserManager.getInstance().getCurrentBhWallet().address)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }
    /**
     * 查询单页方案记录
     */
    public void queryProposals(BaseActivity activity, int page,String title){

        BaseObserver<JsonObject> observer = new BaseObserver<JsonObject>() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                ProposalQueryResult resultInfo = JsonUtils.fromJson(jsonObject.toString(),ProposalQueryResult.class);
                LoadDataModel loadDataModel = new LoadDataModel(resultInfo);
                proposalLiveData.postValue(loadDataModel);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel ldm = new LoadDataModel(LoadingStatus.ERROR,"");
                proposalLiveData.postValue(ldm);
            }
        };
        BHttpApi.getService(BHttpApiInterface.class)
                .queryProposals(page, BHConstants.PAGE_SIZE,title)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }


    public void queryProposalById(BaseActivity activity,String id,boolean showDialog){
        BHProgressObserver<JsonObject> observer = new BHProgressObserver<JsonObject>(activity,showDialog) {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                ProposalInfo proposalInfo = JsonUtils.fromJson(jsonObject.toString(),ProposalInfo.class);
                LoadDataModel loadDataModel = new LoadDataModel(proposalInfo);
                proposalInfoLiveData.postValue(loadDataModel);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel ldm = new LoadDataModel(LoadingStatus.ERROR,"");
                proposalInfoLiveData.postValue(ldm);
            }
        };
        BHttpApi.getService(BHttpApiInterface.class)
                .queryProposal(id)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }

    public void sendDoPledge(BaseActivity activity, final BHSendTranscation bhSendTranscation){
        String body = JsonUtils.toJson(bhSendTranscation);
        LogUtils.d("EnstrustViewModel==>:","body=="+ JsonUtils.toJson(bhSendTranscation));
        BHProgressObserver<JsonObject> observer = new BHProgressObserver<JsonObject>(activity) {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                super.onSuccess(jsonObject);
                LoadDataModel lmd = new LoadDataModel(LoadingStatus.SUCCESS);
                doPledgeLiveData.postValue(lmd);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                //LoadDataModel lmd = new LoadDataModel(LoadingStatus.ERROR);
                LoadDataModel lmd = new LoadDataModel(code,errorMsg);
                doPledgeLiveData.postValue(lmd);
            }
        };

        RequestBody txBody = HUtils.createJson(body);
        TransactionApi.getService(TransactionApiInterface.class)
                .sendTransaction(txBody)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }
    public void sendDoVeto(BaseActivity activity, final BHSendTranscation bhSendTranscation){
        String body = JsonUtils.toJson(bhSendTranscation);
        LogUtils.d("EnstrustViewModel==>:","body=="+ JsonUtils.toJson(bhSendTranscation));
        BHProgressObserver<JsonObject> observer = new BHProgressObserver<JsonObject>(activity) {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                super.onSuccess(jsonObject);
                LoadDataModel lmd = new LoadDataModel(LoadingStatus.SUCCESS);
                doVetoLiveData.postValue(lmd);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                //LoadDataModel lmd = new LoadDataModel(LoadingStatus.ERROR);
                LoadDataModel lmd = new LoadDataModel(code,errorMsg);
                doVetoLiveData.postValue(lmd);
            }
        };

        RequestBody txBody = HUtils.createJson(body);
        TransactionApi.getService(TransactionApiInterface.class)
                .sendTransaction(txBody)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }

    public void sendCreatePorposal(BaseActivity activity, final BHSendTranscation bhSendTranscation){
        String body = JsonUtils.toJson(bhSendTranscation);
        LogUtils.d("EnstrustViewModel==>:","body=="+ JsonUtils.toJson(bhSendTranscation));
        BHProgressObserver<JsonObject> observer = new BHProgressObserver<JsonObject>(activity) {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                super.onSuccess(jsonObject);
                LoadDataModel lmd = new LoadDataModel(LoadingStatus.SUCCESS);
                createProposalLiveData.postValue(lmd);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                //LoadDataModel lmd = new LoadDataModel(LoadingStatus.ERROR);
                LoadDataModel lmd = new LoadDataModel(code,errorMsg);
                createProposalLiveData.postValue(lmd);
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
