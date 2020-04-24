package com.bhex.wallet.balance.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.observer.SimpleObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.google.gson.JsonObject;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/4
 * Time: 0:04
 */
public class BalanceViewModel extends AndroidViewModel implements LifecycleObserver {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private BaseActivity mContext;

    public static MutableLiveData<LoadDataModel<AccountInfo>> accountLiveData  = new MutableLiveData<>();

    public BalanceViewModel(@NonNull Application application) {
        super(application);
    }

    //获取资产
    public void getAccountInfo(BaseActivity activity,String address){
        BHBaseObserver<JsonObject> observer = new BHBaseObserver<JsonObject>() {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                //super.onSuccess(jsonObject);
                AccountInfo accountInfo = JsonUtils.fromJson(jsonObject.toString(),AccountInfo.class);
                LoadDataModel loadDataModel = new LoadDataModel(accountInfo);
                LiveDataBus.getInstance().with(BHConstants.Account_Label,LoadDataModel.class).postValue(loadDataModel);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel loadDataModel = new LoadDataModel(0,"");
                LiveDataBus.getInstance().with(BHConstants.Account_Label,LoadDataModel.class).postValue(loadDataModel);
            }
        };

        BHttpApi.getService(BHttpApiInterface.class)
                .loadAccount(BHUserManager.getInstance().getCurrentBhWallet().address)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }


    private void beginReloadData() {
        Observable.interval(4000,5000L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(mContext, Lifecycle.Event.ON_PAUSE)))
                .subscribe(new SimpleObserver<Long>(){
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        super.onNext(aLong);
                        LogUtils.d("BalanceViewModel===>:","==aLong=="+aLong);
                        BalanceViewModel.this.getAccountInfo(mContext,null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(){
        beginReloadData();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause(){
        if(compositeDisposable!=null && !compositeDisposable.isDisposed()){
            //LogUtils.d("BalanceViewModel===>:","==onPause==");
            compositeDisposable.isDisposed();
        }
    }

    public BalanceViewModel build(BaseActivity context){
        mContext = context;
        return this;
    }

}
