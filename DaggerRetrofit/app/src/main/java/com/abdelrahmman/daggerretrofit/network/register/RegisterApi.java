package com.abdelrahmman.daggerretrofit.network.register;

import com.abdelrahmman.daggerretrofit.models.RegisterResponse;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterApi {

    @FormUrlEncoded
    @POST("createuser")
    Flowable<RegisterResponse> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("school") String school
    );
}
