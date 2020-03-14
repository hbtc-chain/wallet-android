package com.bhex.network.interceptor;

import com.bhex.network.base.INetworkRequiredInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class CommonRequestInterceptor implements Interceptor {

    private INetworkRequiredInfo requiredInfo;
    public CommonRequestInterceptor(INetworkRequiredInfo requiredInfo){
        this.requiredInfo = requiredInfo;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("os", "android");
        builder.addHeader("appVersion",this.requiredInfo.getAppVersionCode());
        return chain.proceed(builder.build());
    }
}
