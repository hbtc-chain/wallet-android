package com.bhex.wallet.balance.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.utils.ToastUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.manager.BHUserManager;
import com.google.gson.JsonObject;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author gongdongyang
 * 2020-9-1 15:36:12
 * 测试币申请
 */
public class TestTokenViewModel extends AndroidViewModel {

    public TestTokenViewModel(@NonNull Application application) {
        super(application);
    }

    //申请测试币
    public void send_test_token(FragmentActivity context, String demon,String demon2){
        String address = BHUserManager.getInstance().getCurrentBhWallet().address;
        BHProgressObserver<JsonObject> observer = new BHProgressObserver<JsonObject>(context) {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                super.onSuccess(jsonObject);
                ToastUtils.showToast(context.getResources().getString(R.string.string_apply_success));
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
            }
        };
        BHttpApi.getService(BHttpApiInterface.class).send_test_token(address,demon)
                .observeOn(Schedulers.io())
                .flatMap(new Function<JsonObject, ObservableSource<JsonObject>>() {
                    @Override
                    public ObservableSource<JsonObject> apply(JsonObject jsonObject) throws Exception {
                        return BHttpApi.getService(BHttpApiInterface.class).send_test_token(address,demon2);
                    }
                })
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(context)))
                .subscribe(observer);
    }
}
