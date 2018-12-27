package com.example.alexr.ideamanager.services;

import android.os.Build;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
        .readTimeout(15000, TimeUnit.SECONDS)
        .addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                request = request.newBuilder()
                    .addHeader("x-device-type", Build.DEVICE)
                    .addHeader("Accept-Language", Locale.getDefault().getLanguage())
                    .build();

                return chain.proceed(request);
            }
        })
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
