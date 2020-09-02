package com.bhex.wallet.bh_main.my.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.utils.ToastUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.manager.BHUserManager;
import com.google.gson.JsonObject;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

/**
 * @author gongdongyang
 * 2020-9-1 15:36:12
 * 测试币申请
 */
public class TestTokenViewModel extends AndroidViewModel {
    //public MutableLiveData<>
    public TestTokenViewModel(@NonNull Application application) {
        super(application);
    }

    //申请测试币
    public void send_test_token(Fragment context){
        String address = BHUserManager.getInstance().getCurrentBhWallet().address;
        BHProgressObserver<JsonObject> observer = new BHProgressObserver<JsonObject>(context.getContext()) {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                super.onSuccess(jsonObject);
                ToastUtils.showToast("申请成功");
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
            }
        };
        BHttpApi.getService(BHttpApiInterface.class).send_test_token(address)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(context)))
                .subscribe(observer);
    }
}
