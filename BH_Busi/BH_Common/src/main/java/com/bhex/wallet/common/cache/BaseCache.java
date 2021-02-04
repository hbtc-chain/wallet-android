package com.bhex.wallet.common.cache;

import com.bhex.network.cache.stategy.CacheStrategy;
import com.bhex.network.cache.stategy.IStrategy;
import com.bhex.network.receiver.NetWorkStatusChangeReceiver;
import com.bhex.tools.utils.LogUtils;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/7
 * Time: 17:59
 */
public class BaseCache implements CacheLisenter {

    public IStrategy getCacheStrategy() {
        if(NetWorkStatusChangeReceiver.getNectworkStatus() == NetWorkStatusChangeReceiver.Network_Status_None){
            return CacheStrategy.cacheAndRemote();
        }else{
            return CacheStrategy.firstRemote();
        }
    }

    public IStrategy getCacheStrategy(IStrategy strategy) {
        if(NetWorkStatusChangeReceiver.getNectworkStatus() == NetWorkStatusChangeReceiver.Network_Status_None){
            return CacheStrategy.cacheAndRemote();
        }else{
            return strategy;
        }
    }


    @Override
    public synchronized void beginLoadCache() {

    }

    @Override
    public synchronized void removeMemoryCache() {

    }
}
