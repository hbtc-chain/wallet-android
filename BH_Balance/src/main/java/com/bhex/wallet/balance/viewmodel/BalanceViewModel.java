package com.bhex.wallet.balance.viewmodel;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.base.viewmodel.CacheAndroidViewModel;
import com.bhex.network.cache.RxCache;
import com.bhex.network.cache.data.CacheResult;
import com.bhex.network.cache.stategy.IStrategy;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.observer.SimpleObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.balance.ui.fragment.BalanceFragment;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.lang.reflect.Type;
import java.util.List;
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
public class BalanceViewModel extends CacheAndroidViewModel implements LifecycleObserver {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private BaseActivity mContext;
    private BaseFragment mFragment;

    public static MutableLiveData<LoadDataModel<AccountInfo>> accountLiveData  = new MutableLiveData<>();

    public BalanceViewModel(@NonNull Application application) {
        super(application);
    }

    /*public class ViewModeFactory implements ViewModelProvider.Factory{

        public ViewModeFactory() {
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return new BalanceViewModel(mContext);
        }
    }*/

    //获取资产
    public void getAccountInfo(BaseActivity activity,IStrategy strategy){

        Type type = (new TypeToken<JsonObject>() {}).getType();
        String cache_key = BHUserManager.getInstance().getCurrentBhWallet().address+"_"+BH_BUSI_TYPE.账户资产缓存.value;
        BHBaseObserver<JsonObject> observer = new BHBaseObserver<JsonObject>(false) {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                //super.onSuccess(jsonObject);
                AccountInfo accountInfo = JsonUtils.fromJson(jsonObject.toString(),AccountInfo.class);
                LoadDataModel loadDataModel = new LoadDataModel(accountInfo);
                if(accountInfo!=null){
                    BHUserManager.getInstance().setAccountInfo(accountInfo);
                }
                LiveDataBus.getInstance().with(BHConstants.Label_Account,LoadDataModel.class).postValue(loadDataModel);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                LiveDataBus.getInstance().with(BHConstants.Label_Account,LoadDataModel.class).postValue(loadDataModel);
            }
        };

        BHttpApi.getService(BHttpApiInterface.class)
                .loadAccount(BHUserManager.getInstance().getCurrentBhWallet().address)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().transformObservable(cache_key,type,getCacheStrategy(strategy)))
                .map(new CacheResult.MapFunc<JsonObject>())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }


    /*public void getRateToken(BaseActivity activity){
        Type type = (new TypeToken<List<BHToken>>() {}).getType();

        BHttpApi.getService(BHttpApiInterface.class).loadSymbol(1,1000)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().<JsonObject>transformObservable("symbol_all", type, strategy))
                .map(new CacheResult.MapFunc<>())
                .subscribe(new BHBaseObserver<JsonObject>(false) {
                    @Override
                    protected void onSuccess(JsonObject jsonObject) {
                        if(!JsonUtils.isHasMember(jsonObject,"items")){
                            return;
                        }
                        symbolMap.clear();
                        List<BHToken> coinList = JsonUtils.getListFromJson(jsonObject.toString(),"items", BHToken.class);
                        //缓存所有的token
                        StringBuffer sb = new StringBuffer();
                        for(BHToken item:coinList){
                            symbolMap.put(item.symbol,item);
                            sb.append(item.symbol).append("_");
                        }
                        if(!TextUtils.isEmpty(sb)){
                            MMKVManager.getInstance().mmkv().encode(BHConstants.SYMBOL_DEFAULT_KEY,sb.toString());
                        }
                    }


                    @Override
                    protected void onFailure(int code, String errorMsg) {
                        super.onFailure(code, errorMsg);
                    }
                });
    }*/


    private void beginReloadData() {
        Observable.interval(4000,5000L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(mContext, Lifecycle.Event.ON_PAUSE)))
                .subscribe(new SimpleObserver<Long>(){
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        if(compositeDisposable==null){
                            compositeDisposable = new CompositeDisposable();
                        }
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        super.onNext(aLong);
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
        Fragment frag = mContext.getSupportFragmentManager().findFragmentByTag(BalanceFragment.class.getSimpleName());
        if(frag!=null && frag.getUserVisibleHint() && !frag.isHidden()){
            beginReloadData();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause(){
        if(compositeDisposable!=null && !compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    public BalanceViewModel build(BaseActivity context){
        mContext = context;
        return this;
    }


}
