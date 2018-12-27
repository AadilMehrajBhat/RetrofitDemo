package com.example.alexr.ideamanager.services;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {

    private static final String BASE_URL = "http://10.0.2.2:9000";

    // Create Logger
    private static HttpLoggingInterceptor mLogger = new HttpLoggingInterceptor()
        .setLevel(Level.BODY);

    // OkHttp Client
    private static OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
        .addInterceptor(mLogger)
        .build();


    private static Retrofit.Builder mBuilder = new Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(mOkHttpClient);

    private static Retrofit mRetrofit = mBuilder.build();

    public static <S> S buildService(Class<S> serviceType) {
        return mRetrofit.create(serviceType);
    }

}
