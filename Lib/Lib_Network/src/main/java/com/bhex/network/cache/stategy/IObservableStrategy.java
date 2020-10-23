package com.bhex.network.cache.stategy;



import com.bhex.network.cache.RxCache;
import com.bhex.network.cache.data.CacheResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;


/**
 * author : zchu
 * date   : 2017/10/11
 * desc   :
 */
public interface IObservableStrategy {

    <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type);
}
