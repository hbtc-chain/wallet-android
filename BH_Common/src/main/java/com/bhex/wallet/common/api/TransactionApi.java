package com.bhex.wallet.common.api;

import com.bhex.network.base.NetworkApi;
import com.bhex.tools.utils.DateUtil;

import java.io.IOException;

import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/1
 * Time: 15:33
 */
/*public class TransactionApi extends NetworkApi {

    private static volatile TransactionApi INSTANCE;

    public static TransactionApi getInstance(){

        if(INSTANCE==null){
            synchronized (TransactionApi.class){
                if(INSTANCE==null){
                    INSTANCE = new TransactionApi();
                }
            }
        }

        return INSTANCE;
    }

    @Override
    protected Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String timeStr = DateUtil.getTimeStr();
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("Source", "source");
                builder.addHeader("Date", timeStr);
                return chain.proceed(builder.build());
            }
        };
    }


    @Override
    protected <T> Function<T, T> getAppErrorHandler() {
        return new Function<T, T>() {
            @Override
            public T apply(T response) throws Exception {
                *//*if (response instanceof BaseResponse && ((BaseResponse) response).code != 0) {
                    ExceptionHandler.ServerException exception = new ExceptionHandler.ServerException();
                    exception.code = ((BaseResponse) response).code;
                    exception.message = ((BaseResponse) response).message != null ? ((BaseResponse) response).message : "";
                    throw exception;
                }*//*
                return response;
            }
        };
    }
    public static  <T> T getService(Class<T> service) {
        return getInstance().getRetrofit(service).create(service);
    }

    @Override
    public String getFormal() {
        //return "http://bhchain-testnet-node1-751700059.ap-northeast-1.elb.amazonaws.com:1317/";
//        return "http://public-chain-web-1081294751.ap-northeast-1.elb.amazonaws.com:11994/";
        return "http://public-chain-mainnet-631149863.ap-northeast-1.elb.amazonaws.com:26657/";
    }

    @Override
    public String getTest() {
        //return "http://bhchain-testnet-node1-751700059.ap-northeast-1.elb.amazonaws.com:1317/";
//        return "http://public-chain-web-1081294751.ap-northeast-1.elb.amazonaws.com:11994/";
        return "http://public-chain-mainnet-631149863.ap-northeast-1.elb.amazonaws.com:26657/";
    }
}*/
