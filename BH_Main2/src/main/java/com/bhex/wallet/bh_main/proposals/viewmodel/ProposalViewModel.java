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
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.model.ProposalQueryResult;
import com.bhex.wallet.common.model.ValidatorInfo;
import com.bhex.wallet.common.tx.TransactionOrder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.List;

/**
 * Created by BHEX.
 * User: zc
 * Date: 2020/4/16
 */
public class ProposalViewModel extends ViewModel {

    public MutableLiveData<LoadDataModel<ProposalQueryResult>> proposalLiveData = new MutableLiveData<>();

    /**
     * 查询单页方案记录
     */
    public void queryTransctionByAddress(BaseActivity activity,int page){

        BaseObserver<JsonObject> observer = new BaseObserver<JsonObject>() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                ProposalQueryResult accountInfo = JsonUtils.fromJson(jsonObject.toString(),ProposalQueryResult.class);
                LoadDataModel loadDataModel = new LoadDataModel(accountInfo);
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
                .queryProposals(page, BHConstants.PAGE_SIZE)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }

}
