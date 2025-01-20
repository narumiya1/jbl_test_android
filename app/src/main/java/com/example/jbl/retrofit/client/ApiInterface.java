package com.example.jbl.retrofit.client;

import com.example.jbl.DataItem;
import com.example.jbl.ResponseUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/api/unknown")
    Call<ResponseUser> doGetListResources();

    @POST("/api/users")
    Call<DataItem> createUser(@Body DataItem user);

    @GET("/api/users?")
    Call<ResponseUser> doGetUserList(@Query("page") String page);

    @FormUrlEncoded
    @POST("/api/users?")
    Call<ResponseUser> doCreateUserWithField(@Field("name") String name, @Field("job") String job);

}
