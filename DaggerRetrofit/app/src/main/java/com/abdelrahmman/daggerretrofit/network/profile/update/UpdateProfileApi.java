package com.abdelrahmman.daggerretrofit.network.profile.update;

import com.abdelrahmman.daggerretrofit.models.LoginResponse;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UpdateProfileApi {

    @FormUrlEncoded
    @PUT("updateuser/{id}")
    Flowable<LoginResponse> updateUser(
            @Path("id") int id,
            @Field("email") String email,
            @Field("name") String name,
            @Field("school") String school
    );

}
