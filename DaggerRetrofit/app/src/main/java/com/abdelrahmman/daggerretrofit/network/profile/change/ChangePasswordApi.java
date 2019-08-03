package com.abdelrahmman.daggerretrofit.network.profile.change;

import com.abdelrahmman.daggerretrofit.models.DefaultResponse;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;

public interface ChangePasswordApi {

    @FormUrlEncoded
    @PUT("updatepassword")
    Flowable<DefaultResponse> changePassword(
            @Field("current_password") String current_password,
            @Field("new_password") String new_password,
            @Field("email") String email
    );

}
