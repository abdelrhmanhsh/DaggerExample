package com.abdelrahmman.daggerretrofit.network.profile.delete;

import com.abdelrahmman.daggerretrofit.models.DefaultResponse;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface DeleteAccountApi {

    @DELETE("deleteuser/{id}")
    Flowable<DefaultResponse> deleteUser(@Path("id") int id);

}
