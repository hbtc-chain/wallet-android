package com.bhex.wallet.common.cache;

import android.text.TextUtils;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.cache.RxCache;
import com.bhex.network.cache.data.CacheResult;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.model.BHTokenMapping;
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

/**
 * @author gongdongyang
 * 2020-10-10 14:15:35
 */
public class TokenMapCache extends BaseCache {

    private static final String TAG = TokenMapCache.class.getSimpleName();

    public static final String CACHE_KEY = "TokenMapCache";

    private LinkedHashMap<String,BHTokenMapping> mTokenMappings = new LinkedHashMap<>();


    private static volatile TokenMapCache _instance;

    private TokenMapCache(){

    }

    public static TokenMapCache getInstance(){
        if(_instance==null){
            _instance = new TokenMapCache();
        }
        return _instance;
    }

    @Override
    public void beginLoadCache() {
        super.beginLoadCache();
        loadTokenMapping();
    }

    private void loadTokenMapping() {

        Type type = (new TypeToken<JsonObject>() {}).getType();

        BHttpApi.getService(BHttpApiInterface.class).loadTokenMappings()
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().transformObservable(CACHE_KEY, type,getCacheStrategy()))
                .map(new CacheResult.MapFunc())
                .subscribe(new BHBaseObserver<JsonObject>(false) {
                    @Override
                    protected void onSuccess(JsonObject jsonObject) {
                        if(!JsonUtils.isHasMember(jsonObject,"items")){
                            return;
                        }
                        mTokenMappings.clear();
                        List<BHTokenMapping> tokens = JsonUtils.getListFromJson(jsonObject.toString(),"items", BHTokenMapping.class);
                        //缓存所有的token
                        if(ToolUtils.checkListIsEmpty(tokens)){
                            return;
                        }

                        for (BHTokenMapping item:tokens) {
                            mTokenMappings.put(item.issue_symbol,item);
                            BHTokenMapping reverseItem = new BHTokenMapping(item.target_symbol,item.issue_symbol,item.total_supply,item.issue_pool,item.enabled);
                            mTokenMappings.put(reverseItem.issue_symbol,reverseItem);
                        }

                        LogUtils.d("TokenMapCache==>:","=mTokenMappings="+mTokenMappings.size());
                    }


                    @Override
                    protected void onFailure(int code, String errorMsg) {
                        super.onFailure(code, errorMsg);
                    }
                });
    }

    public  BHTokenMapping getTokenMapping(String symbol){
        return  mTokenMappings.get(symbol.toLowerCase());
    }

    public List<BHTokenMapping> getTokenMappings() {
        return new ArrayList<>(mTokenMappings.values());
    }
}
