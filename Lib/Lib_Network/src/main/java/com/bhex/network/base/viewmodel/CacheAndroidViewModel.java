package com.bhex.network.base.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bhex.network.cache.stategy.CacheStrategy;
import com.bhex.network.cache.stategy.IStrategy;
import com.bhex.network.receiver.NetWorkStatusChangeReceiver;

/**
 * @author gongdongyang
 * 2020-5-30 14:48:50
 */
public class CacheAndroidViewModel extends AndroidViewModel {

    public CacheAndroidViewModel(@NonNull Application application) {
        super(application);
    }

    public IStrategy getCacheStrategy() {
        return (NetWorkStatusChangeReceiver.getNectworkStatus() == NetWorkStatusChangeReceiver.Network_Status_None)
                ? CacheStrategy.firstCache() : CacheStrategy.cacheAndRemote();
    }

    public IStrategy getCacheStrategy(IStrategy strategy) {
        if(NetWorkStatusChangeReceiver.getNectworkStatus() == NetWorkStatusChangeReceiver.Network_Status_None){
            return CacheStrategy.firstCache();
        }else if(strategy!=null){
            return strategy;
        }else {
            return CacheStrategy.cacheAndRemote();
        }
    }
}
