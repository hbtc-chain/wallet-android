package com.bhex.network.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class CommonResponseInterceptor implements Interceptor {

    private static final String TAG = "ResponseInterceptor";
    @Override
    public Response intercept(Chain chain) throws IOException {
        long requestTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        Log.d(TAG, "requestTime="+ (System.currentTimeMillis() - requestTime));
        return response;
    }
}
