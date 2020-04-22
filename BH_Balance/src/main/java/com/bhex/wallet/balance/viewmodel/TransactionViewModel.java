package com.bhex.wallet.balance.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.observer.SimpleObserver;
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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/1
 * Time: 15:23
 */
public class TransactionViewModel extends AndroidViewModel implements LifecycleObserver {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private BaseActivity mActivity;
    private String mSymbol;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<LoadDataModel> mutableLiveData  = new MutableLiveData<>();
    public MutableLiveData<LoadDataModel<List<TransactionOrder>>> transLiveData  = new MutableLiveData<>();


    public void initData(BaseActivity activity,String symbol){
        mActivity = activity;
        mSymbol = symbol;
    }
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
                LoadDataModel lmd = new LoadDataModel();
                mutableLiveData.postValue(lmd);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel lmd = new LoadDataModel(code,"");
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


    private void beginReloadData() {
        Observable.interval(4000,5000L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtils.d("TransactionViewModel===>:","aLong=="+aLong);
                        //queryTransctionByAddress();
                        TransactionViewModel.this.queryTransctionByAddress(mActivity,
                                BHUserManager.getInstance().getCurrentBhWallet().address,1,mSymbol,null);
                    }

                    @Override
                    public void onError(Throwable e) {

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
            compositeDisposable.clear();
        }
    }

}
