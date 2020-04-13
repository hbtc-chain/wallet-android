package com.bhex.wallet.common.cache;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.cache.RxCache;
import com.bhex.network.cache.data.CacheResult;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.model.BHToken;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/7
 * Time: 17:57
 */
public class SymbolCache extends BaseCache {

    private static final String TAG = SymbolCache.class.getSimpleName();

    public static final String CACHE_KEY = "SymbolCache";


    private Map<String, BHToken> symbolMap = new ConcurrentHashMap();

    private static volatile SymbolCache _instance;

    private SymbolCache(){

    }

    public static SymbolCache getInstance(){
        if(_instance==null){
            _instance = new SymbolCache();
        }
        return _instance;
    }

    @Override
    public void beginLoadCache() {
        Type type = (new TypeToken<List<BHToken>>() {}).getType();



        BHttpApi.getService(BHttpApiInterface.class).loadSymbol(1,2000)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().transformObservable("symbol_all", type, getCacheStrategy()))
                .map(new CacheResult.MapFunc<>())
                .subscribe(new BHBaseObserver<JsonObject>() {
                    @Override
                    protected void onSuccess(JsonObject jsonObject) {
                        symbolMap.clear();
                        List<BHToken> coinList = JsonUtils.getListFromJson(jsonObject.toString(),"tokens", BHToken.class);
                        for(BHToken item:coinList){
                            symbolMap.put(item.symbol,item);
                        }

                        //LogUtils.d(TAG+"===>:","size=="+symbolMap.size());
                    }


                    @Override
                    protected void onFailure(int code, String errorMsg) {
                        super.onFailure(code, errorMsg);
                    }
                });
    }


    public  BHToken getBHToken(String symbol){
        //LogUtils.d(TAG+"====>:","size=="+symbolMap.size());
        return symbolMap.get(symbol);
    }
}
