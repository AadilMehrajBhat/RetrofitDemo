package com.example.aadilmehraj.ideamanager.services;

import com.example.aadilmehraj.ideamanager.models.Idea;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface IdeaService {


    @GET("ideas")
    Call<List<Idea>> getIdeas(@Query("owner") String owner, @Query("count") int count);

    @GET("ideas")
    Call<List<Idea>> getIdeas(@QueryMap HashMap<String, String> filters);

//    @Headers("x-device-type: Android")
    @GET("ideas/{id}")
    Call<Idea> getIdea(@Path("id") int id /* , @Header("Accept-Language") String language */);

    @POST("ideas")
    Call<Idea> createIdea(@Body Idea newIdea);

    @FormUrlEncoded
    @PUT("ideas/{id}")
    Call<Idea> updateIdea(
        @Path("id") int id,
        @Field("name") String name,
        @Field("description") String desc,
        @Field("status") String status,
        @Field("owner") String owner
    );

    @DELETE("ideas/{id}")
    Call<Void> deleteIdea(@Path("id") int id);
}
