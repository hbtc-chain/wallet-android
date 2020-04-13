package com.bhex.wallet.common.cache;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/7
 * Time: 17:53
 */
public interface CacheLisenter {
    void beginLoadCache();

    void removeMemoryCache();
}
