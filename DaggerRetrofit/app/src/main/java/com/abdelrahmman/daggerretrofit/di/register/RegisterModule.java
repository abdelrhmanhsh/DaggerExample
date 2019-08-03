package com.abdelrahmman.daggerretrofit.di.register;

import com.abdelrahmman.daggerretrofit.network.register.RegisterApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class RegisterModule {

    @RegisterScope
    @Provides
    static RegisterApi provideRegisterApi(Retrofit retrofit){
        return retrofit.create(RegisterApi.class);
    }
}
