package com.bhex.wallet.bh_main.validator.viewmodel;

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
import com.bhex.wallet.common.model.ValidatorInfo;
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
public class ValidatorViewModel extends ViewModel {

    public MutableLiveData<LoadDataModel<List<ValidatorInfo>>> validatorLiveData  = new MutableLiveData<>();

    //获取验证人
    public void getValidatorInfo(BaseActivity activity,int valid){
        BHProgressObserver<JsonArray> observer = new BHProgressObserver<JsonArray>(activity) {
            @Override
            protected void onSuccess(JsonArray jsonObject) {
                super.onSuccess(jsonObject);
                List<ValidatorInfo> validatorInfoList = JsonUtils.getListFromJson(jsonObject.toString(), ValidatorInfo.class);
                LoadDataModel loadDataModel = new LoadDataModel(validatorInfoList);
                validatorLiveData.postValue(loadDataModel);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                validatorLiveData.postValue(loadDataModel);
            }
        };

        BHttpApi.getService(BHttpApiInterface.class)
                .queryValidators(valid)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }


}
