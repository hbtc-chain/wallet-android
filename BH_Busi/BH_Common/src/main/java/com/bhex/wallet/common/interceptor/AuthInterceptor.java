package com.bhex.wallet.common.interceptor;

import com.bhex.network.app.BaseApplication;
import com.bhex.tools.language.LocalManageUtil;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author gongdongyang
 * 2020-5-8 18:51:00
 *
 */
public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("content-type", "application/json;charset:utf-8");
        Locale locale = LocalManageUtil.getSetLanguageLocale(BaseApplication.getInstance());
        builder.addHeader("locale",locale.getLanguage());
        builder.addHeader("Accept-Language",locale.getLanguage());
        return chain.proceed(builder.build());
    }
}

