package com.bhex.wallet.balance.viewmodel;

import android.util.ArrayMap;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.helper.CoinSearchHelper;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.db.AppDataBase;
import com.bhex.wallet.common.db.dao.BHTokenDao;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.common.model.BHToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author gongdongyang
 * 2020年10月31日11:18:48
 */
public class TokenViewModel extends ViewModel {

    private BHTokenDao bhTokenDao = AppDataBase.getInstance(BaseApplication.getInstance()).bhTokenDao();

    public MutableLiveData<LoadDataModel> searchLiveData  = new MutableLiveData<>();
    public MutableLiveData<LoadDataModel> queryLiveData  = new MutableLiveData<>();

    public TokenViewModel(){

    }

    public void search_token(BaseActivity activity,String search_key,String chain){
        BHBaseObserver<JsonArray> observer = new BHBaseObserver<JsonArray>() {
            @Override
            protected void onSuccess(JsonArray jsonArray) {
                if(jsonArray==null){
                    return;
                }
                List<BHToken> bhToken = JsonUtils.getListFromJson(jsonArray.toString(), BHToken.class);
                LoadDataModel ldm = new LoadDataModel(bhToken);
                searchLiveData.postValue(ldm);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel ldm = new LoadDataModel(LoadingStatus.ERROR,errorMsg);
                searchLiveData.postValue(ldm);
            }
        };
        BHttpApi.getService(BHttpApiInterface.class)
                .searchToken(chain,search_key)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }

    public  void addOrCancelToken(boolean checked, BHToken bhToken) {
        ArrayMap<String,BHToken> map_tokens = CacheCenter.getInstance().getSymbolCache().getDefaultToken();
        //添加资产
        if(checked){
            SymbolCache.getInstance().addBHToken(bhToken);
            //判断币种是否在list中
            if(map_tokens.get(bhToken.symbol)!=null){
                //保存记录与数据库中
                saveTokenToDb(bhToken);
            }
            map_tokens.put(bhToken.symbol,bhToken);
        }
        //取消资产
        if(!checked){
            if(map_tokens.get(bhToken.symbol)!=null){
                map_tokens.remove(bhToken.symbol);
            }
        }

        if(ToolUtils.checkMapEmpty(map_tokens)){
            MMKVManager.getInstance().mmkv().remove(BHConstants.SYMBOL_DEFAULT_KEY);
            return;
        }
        //保存于
        StringBuffer v_token = new StringBuffer("");
        for (ArrayMap.Entry<String,BHToken> item:map_tokens.entrySet()) {
            v_token.append(item.getValue().symbol).append("_");
        }
        MMKVManager.getInstance().mmkv().encode(BHConstants.SYMBOL_DEFAULT_KEY, v_token.toString());
    }

    public void saveTokenToDb(BHToken bhToken){
        Observable.create(emitter -> {
            Long res = bhTokenDao.insertSingle(bhToken);
            emitter.onNext(res+"");
            emitter.onComplete();
        }).compose(RxSchedulersHelper.io_main())
          .subscribe(v->{

          });
    }

    public void queryToken(FragmentActivity activity,String symbol){
        BHBaseObserver<BHToken> observer = new BHBaseObserver<BHToken>() {
            @Override
            protected void onSuccess(BHToken bhToken) {
                LoadDataModel ldm = new LoadDataModel(bhToken);
                queryLiveData.postValue(ldm);
                //保存Token到库中
                saveTokenToDb(bhToken);
                SymbolCache.getInstance().addBHToken(bhToken);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel ldm = new LoadDataModel(LoadingStatus.ERROR,errorMsg);
                queryLiveData.postValue(ldm);
            }
        };
        BHttpApi.getService(BHttpApiInterface.class)
                .queryToken(symbol)
                .compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }
}
