package com.bhex.wallet.api;

import com.bhex.network.Urls;
import com.bhex.network.base.BaseResponse;
import com.bhex.network.base.NetworkApi;
import com.bhex.network.errorhandler.ExceptionHandler;
import com.bhex.tools.utils.TimeUtils;

import java.io.IOException;

import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class BHNetworkApi extends NetworkApi {

    private static volatile BHNetworkApi INSTANCE;

    public static BHNetworkApi getInstance(){

        if(INSTANCE==null){
            synchronized (BHNetworkApi.class){
                if(INSTANCE==null){
                    INSTANCE = new BHNetworkApi();
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
                String timeStr = TimeUtils.getTimeStr();
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
                if (response instanceof BaseResponse && ((BaseResponse) response).code != 0) {
                    ExceptionHandler.ServerException exception = new ExceptionHandler.ServerException();
                    exception.code = ((BaseResponse) response).code;
                    exception.message = ((BaseResponse) response).message != null ? ((BaseResponse) response).message : "";
                    throw exception;
                }
                return response;
            }
        };
    }

    public static  <T> T getService(Class<T> service) {
        return getInstance().getRetrofit(service).create(service);
    }

    @Override
    public String getFormal() {
        return Urls.BASE_ONLINE_URL;
    }

    @Override
    public String getTest() {
        return Urls.BASE_OFFLINE_URL;
    }
}
