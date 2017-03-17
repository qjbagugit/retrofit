package com.example.retrofit.rxjava;

import android.util.ArrayMap;

import com.example.retrofit.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chenping on 2017/3/17.
 */

public class RetrofitClient {

    private static final int READ_TIME_OUT = 15;
    private static final int WRITE_TIME_OUT = 15;
    private static final int CONN_TIME_OUT = 15;
    private static final String BASE_URL = BuildConfig.baseUrl;

    private static ArrayMap<String, String> headers;
    private static ArrayMap<String, String> params;

    private static Retrofit retrofit;

    static {
        headers = new ArrayMap<>();
        // 设置公共头
        params = new ArrayMap<>();
        // 设置公共参数
    }

    private RetrofitClient() {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        httpBuilder.addInterceptor(new AppInterceptor(headers,params))
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONN_TIME_OUT,TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8,15,TimeUnit.SECONDS))
                .retryOnConnectionFailure(false);

//        httpBuilder.socketFactory(HttpsHelper.getSSLSocketFactory(context, new Integer[]{R.raw.xxx}));
//        httpBuilder.hostnameVerifier(HttpsHelper.getHostnameVerifier(new String[]{"",""}));

        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpBuilder.addInterceptor(logInterceptor);
        }
        retrofit = new Retrofit.Builder()
                .client(httpBuilder.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <T> T create(final Class<T> service){
        return retrofit.create(service);
    }

    private static class SingletonHolder{
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance(){
        return SingletonHolder.INSTANCE;
    }

}
