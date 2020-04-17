package com.bhex.wallet.common.api;

import com.bhex.wallet.common.model.BHRates;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/9
 * Time: 20:22
 */
public interface BHttpApiInterface {

    @GET("api/v1/tokens")
    Observable<JsonObject> loadSymbol(@Query("page") int page,@Query("page_size") int pageSize);


    @GET("api/v1/cus/{address}")
    Observable<JsonObject> loadAccount(@Path("address") String address);

    //汇率接口
    @GET("api/v1/tokenprices")
    Observable<List<BHRates>> loadRates(@Query("symbols") String symbols);

    //查询所有交易记录

    /**
     * 某种类型的交易，传空或0为所有交易。0 - 所有交易，1 - 代币转移，2 - 跨链交易，3 - 委托，4 - 收益分配，5 - 链上治理，6 - 削减
     * @param address
     * @return
     */
    @GET("api/v1/cus/{address}/txs")
    Observable<JsonObject> queryTransctionByAddress(@Path("address") String address,
                                                    @Query("page") int page,@Query("page_size") int pageSize,
                                                    @Query("token") String token,@Query("type") String type);


    //验证人接口
    @GET("api/v1/validators")
    Observable<JsonArray> queryValidators(@Query("valid") int valid);
}
