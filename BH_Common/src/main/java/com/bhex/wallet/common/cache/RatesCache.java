package com.bhex.wallet.common.cache;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.cache.RxCache;
import com.bhex.network.cache.data.CacheResult;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHRates;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/10
 * Time: 11:05
 */
public class RatesCache extends BaseCache {

    private static final String TAG = RatesCache.class.getSimpleName();

    public  static final String CACHE_KEY = "RatesCache";

    private Map<String, BHRates.RatesBean> ratesMap = new ConcurrentHashMap();

    private static volatile RatesCache _instance;

    private RatesCache(){

    }

    public static RatesCache getInstance(){
        if(_instance==null){
            _instance = new RatesCache();
        }
        return _instance;
    }

    @Override
    public void beginLoadCache() {
        super.beginLoadCache();
        Type type = (new TypeToken<List<BHRates>>() {}).getType();
        String balacne_list = BHUserManager.getInstance().getSymbolList();

        LogUtils.d("RatesCache===>","balacne_list=="+balacne_list);

        balacne_list = balacne_list.replace("_",",").toUpperCase();

        BHttpApi.getService(BHttpApiInterface.class).loadRates(balacne_list)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().transformObservable("rates_all", type, getCacheStrategy()))
                .map(new CacheResult.MapFunc<>())
                .subscribe(new BHBaseObserver<List<BHRates>>() {
                    @Override
                    protected void onSuccess(List<BHRates> ratelist) {
                        if(ratelist==null || ratelist.size()==0){
                            return;
                        }
                        ratesMap.clear();
                        for (BHRates rate:ratelist){
                            ratesMap.put(rate.getToken(),rate.getRates());
                        }
                        //LogUtils.d(TAG+"===>:","size=="+ratesMap.size());
                    }

                    @Override
                    protected void onFailure(int code, String errorMsg) {
                        super.onFailure(code, errorMsg);
                    }
                });
    }


    /**
     * 获取汇率
     * @return
     */
    public BHRates.RatesBean getBHRate(String symbol){
        return ratesMap.get(symbol);
    }
}
