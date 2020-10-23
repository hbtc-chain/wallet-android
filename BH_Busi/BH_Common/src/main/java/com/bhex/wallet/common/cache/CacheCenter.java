package com.bhex.wallet.common.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/7
 * Time: 17:51
 */
public class CacheCenter {

    private static Map<String, CacheLisenter> cacheLisenterMap = new HashMap();

    private static CacheCenter instance;

    public static CacheCenter getInstance() {
        if(instance==null){
            synchronized (CacheCenter.class){
                instance = new CacheCenter();
            }
        }
        return instance;
    }

    private CacheCenter(){
        cacheLisenterMap.put(SymbolCache.CACHE_KEY, SymbolCache.getInstance());
        cacheLisenterMap.put(RatesCache.CACHE_KEY, RatesCache.getInstance());
        cacheLisenterMap.put(TokenMapCache.CACHE_KEY,TokenMapCache.getInstance());
    }

    /**
     * Symbol缓存
     * @return
     */
    public SymbolCache getSymbolCache(){
        if (cacheLisenterMap.containsKey(SymbolCache.CACHE_KEY)){
            return (SymbolCache)cacheLisenterMap.get(SymbolCache.CACHE_KEY);
        }
        //SymbolCache symbolCache = new SymbolCache();
        cacheLisenterMap.put(SymbolCache.CACHE_KEY, SymbolCache.getInstance());
        return (SymbolCache)cacheLisenterMap.get(SymbolCache.CACHE_KEY);
    }

    public RatesCache getRatesCache(){
        if (cacheLisenterMap.containsKey(RatesCache.CACHE_KEY)){
            return (RatesCache)cacheLisenterMap.get(RatesCache.CACHE_KEY);
        }
        cacheLisenterMap.put(RatesCache.CACHE_KEY, RatesCache.getInstance());
        return (RatesCache)cacheLisenterMap.get(RatesCache.CACHE_KEY);
    }

    public TokenMapCache getTokenMapCache(){
        if (cacheLisenterMap.containsKey(TokenMapCache.CACHE_KEY)){
            return (TokenMapCache)cacheLisenterMap.get(TokenMapCache.CACHE_KEY);
        }
        cacheLisenterMap.put(RatesCache.CACHE_KEY, TokenMapCache.getInstance());
        return (TokenMapCache)cacheLisenterMap.get(TokenMapCache.CACHE_KEY);
    }

    /**
     * 加载缓存信息
     */
    public void loadCache(){
        for (String key : cacheLisenterMap.keySet()) {
            if (cacheLisenterMap.get(key) instanceof CacheLisenter){
                cacheLisenterMap.get(key).beginLoadCache();
            }
        }
    }
}
