package com.example.retrofit.rxjava;

import android.util.ArrayMap;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chenping on 2017/3/17.
 */

public class AppInterceptor implements Interceptor {

    private ArrayMap<String, String> headers;
    private ArrayMap<String, String> params;

    public AppInterceptor(ArrayMap<String, String> headers) {
        this(headers, null);
    }

    public AppInterceptor(ArrayMap<String, String> headers, ArrayMap<String, String> params) {
        this.headers = headers;
        this.params = params;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder requestBuilder = chain.request().newBuilder();
        // 添加公共头
        if (headers!=null && headers.size()>0){
            for (ArrayMap.Entry<String, String> header : headers.entrySet()){
                requestBuilder.addHeader(header.getKey(), header.getValue());
            }
        }
        // 添加公共参数
        HttpUrl.Builder httpurlBuilder = chain.request().url().newBuilder();
        if (params!=null && params.size()>0){
            for (ArrayMap.Entry<String, String> param : params.entrySet()){
                httpurlBuilder.addQueryParameter(param.getKey(), param.getValue());
            }
        }

        return chain.proceed(requestBuilder.url(httpurlBuilder.build()).build());
    }
}
