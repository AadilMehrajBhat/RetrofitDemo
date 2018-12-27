package com.example.alexr.ideamanager.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface MessageService {

    @GET("messages")
    Call<String> getMessage();

    @GET
    Call<String> getMessage(@Url String url);
}
