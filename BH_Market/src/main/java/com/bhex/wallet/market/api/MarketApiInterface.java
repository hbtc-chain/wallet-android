package com.bhex.wallet.market.api;


import com.bhex.wallet.market.model.MarketAllEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public interface MarketApiInterface {
    @GET("api/r/v1/market/all")
    Observable<MarketAllEntity> getMarketAllData();
}
