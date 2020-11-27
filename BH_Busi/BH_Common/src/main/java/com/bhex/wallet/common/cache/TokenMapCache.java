package com.bhex.wallet.common.cache;

import android.text.TextUtils;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.cache.RxCache;
import com.bhex.network.cache.data.CacheResult;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.R;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHChain;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.model.BHTokenMapping;
import com.bhex.wallet.common.model.GasFee;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import java8.util.stream.IntStreams;
import java8.util.stream.RefStreams;
import java8.util.stream.StreamSupport;

/**
 * @author gongdongyang
 * 2020-10-10 14:15:35
 */
public class TokenMapCache extends BaseCache {

    private static final String TAG = TokenMapCache.class.getSimpleName();

    public static final String CACHE_TOKENMAP_KEY = "TokenMappingCache";
    public static final String CACHE_CHAIN_KEY = "TokenChainCache";
    public static final String CACHE_GASFEE_KEY = "GasFeeCache";

    private List<BHTokenMapping> mTokenMappings = new ArrayList<>();
    private List<BHChain> mChains = new ArrayList<>();

    private static volatile TokenMapCache _instance;

    private TokenMapCache(){

    }

    public static TokenMapCache getInstance(){
        if(_instance==null){
            synchronized (TokenMapCache.class){
                if(_instance==null){
                    _instance = new TokenMapCache();
                }
            }
        }
        return _instance;
    }

    @Override
    public synchronized void beginLoadCache() {
        super.beginLoadCache();
        loadTokenMapping();
        loadChain();
        loadDefaultGasFee();
    }

    private synchronized void loadTokenMapping() {

        Type type = (new TypeToken<JsonObject>() {}).getType();

        BHttpApi.getService(BHttpApiInterface.class).loadTokenMappings()
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().transformObservable(CACHE_TOKENMAP_KEY, type,getCacheStrategy()))
                .map(new CacheResult.MapFunc())
                .subscribe(new BHBaseObserver<JsonObject>(false) {
                    @Override
                    protected void onSuccess(JsonObject jsonObject) {
                        if(!JsonUtils.isHasMember(jsonObject,"items")){
                            return;
                        }
                        List<BHTokenMapping> tokens = JsonUtils.getListFromJson(jsonObject.toString(),"items", BHTokenMapping.class);
                        //缓存所有的token
                        if(ToolUtils.checkListIsEmpty(tokens)){
                            return;
                        }

                        mTokenMappings.clear();

                        for (BHTokenMapping item:tokens) {
                            if(!item.enabled){
                                continue;
                            }
                            item.coin_symbol = item.issue_symbol;
                            mTokenMappings.add(item);
                            BHTokenMapping reverseItem = new BHTokenMapping(item.target_symbol,item.issue_symbol,item.issue_symbol,item.total_supply,item.issue_pool,item.enabled);
                            mTokenMappings.add(reverseItem);
                        }

                        LogUtils.d("TokenMapCache==>:","=mTokenMappings="+mTokenMappings.size());
                    }


                    @Override
                    protected void onFailure(int code, String errorMsg) {
                        super.onFailure(code, errorMsg);
                    }
                });
    }

    private synchronized void loadChain() {
        BHBaseObserver observer = new BHBaseObserver<JsonArray>() {
            @Override
            protected void onSuccess(JsonArray jsonObject) {
                if(TextUtils.isEmpty(jsonObject.toString())){
                    return;
                }

                List<BHChain> chains = JsonUtils.getListFromJson(jsonObject.toString(),BHChain.class);
                if(ToolUtils.checkListIsEmpty(chains)){
                    return;
                }
                mChains = chains;
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);

            }

        };

        Type type = (new TypeToken<JsonObject>() {}).getType();
        BHttpApi.getService(BHttpApiInterface.class).loadChain()
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().transformObservable(CACHE_CHAIN_KEY, type,getCacheStrategy()))
                .map(new CacheResult.MapFunc())
                .subscribe(observer);
    }

    private synchronized void loadDefaultGasFee() {
        BHBaseObserver observer = new BHBaseObserver<GasFee>() {
            @Override
            protected void onSuccess(GasFee gasFee) {
                LogUtils.d("TOkenMapCache===",gasFee.fee+"==gasFee=="+gasFee.displayFee);
                BHUserManager.getInstance().gasFee = new GasFee(gasFee.fee,gasFee.gas);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);

            }
        };

        Type type = (new TypeToken<GasFee>() {}).getType();
        BHttpApi.getService(BHttpApiInterface.class).queryGasfee()
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().transformObservable(CACHE_GASFEE_KEY, type,getCacheStrategy()))
                .map(new CacheResult.MapFunc())
                .subscribe(observer);
    }

    public synchronized List<BHTokenMapping> getTokenMapping(String symbol){
        List<BHTokenMapping> res = new ArrayList<>();
        for(BHTokenMapping item:mTokenMappings){
            if(item.coin_symbol.equalsIgnoreCase(symbol)){
                res.add(item);
            }
        }
        return res;
    }

    public synchronized List<BHChain> loadChains(){
        if(!ToolUtils.checkListIsEmpty(mChains)){
            return mChains;
        }
        String[] chain_list = BHUserManager.getInstance().getUserBalanceList().split("_");
        String[] default_chain_name = BaseApplication.getInstance().getResources().getStringArray(R.array.default_chain_name);
        /*for (int i = 0; i < chain_list.length; i++) {
            BHChain bhChain = new BHChain(chain_list[i],default_chain_name[i]);
            mChains.add(bhChain);
        }*/
        IntStreams.range(0,chain_list.length).forEach(value -> {
            BHChain bhChain = new BHChain(chain_list[value],default_chain_name[value]);
            mChains.add(bhChain);
        });
        return mChains;
    }

    public synchronized BHTokenMapping getTokenMappingOne(String symbol){
        for(BHTokenMapping item:mTokenMappings){
            if(item.coin_symbol.equalsIgnoreCase(symbol)){
                return item;
            }
        }
        /* RefStreams.of(mTokenMappings).filter(item->{
             item.coin_symbol.equalsIgnoreCase(symbol);
        }).collect();*/

        return null;
    }

    public synchronized List<BHTokenMapping> getTokenMappings() {
        LinkedHashMap<String,BHTokenMapping> maps = new LinkedHashMap<>();
        for (BHTokenMapping item:mTokenMappings) {
            if(maps.get(item.coin_symbol)!=null){
                continue;
            }
            maps.put(item.coin_symbol,item);
        }
        return new ArrayList<>(maps.values());
    }
}
