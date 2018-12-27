package com.example.alexr.ideamanager.services;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {

    private static final String BASE_URL = "http://10.0.2.2:9000";
    private static Retrofit.Builder mBuilder = new Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit mRetrofit = mBuilder.build();

    public static <S> S buildService(Class<S> serviceType) {
        return mRetrofit.create(serviceType);
    }

}
