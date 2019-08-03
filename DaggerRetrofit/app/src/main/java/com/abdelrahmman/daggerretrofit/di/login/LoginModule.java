package com.abdelrahmman.daggerretrofit.di.login;

import com.abdelrahmman.daggerretrofit.network.login.LoginApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class LoginModule {

    @LoginScope
    @Provides
    static LoginApi provideLoginApi(Retrofit retrofit){
        return retrofit.create(LoginApi.class);
    }

}
