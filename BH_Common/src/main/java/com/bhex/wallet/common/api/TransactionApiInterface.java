package com.bhex.wallet.common.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/1
 * Time: 15:37
 */
public interface TransactionApiInterface {

    @POST("txs")
    Observable<JsonObject> sendTransaction(@Body RequestBody body);

    @GET("cu/cus/{address}")
    Observable<JsonObject> getTransactionSequence(@Path("address") String address);

    @GET("token/listalltokens")
    Observable<JsonObject> loadSymbol();
}
