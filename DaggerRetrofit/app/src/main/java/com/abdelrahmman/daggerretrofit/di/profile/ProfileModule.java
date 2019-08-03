package com.abdelrahmman.daggerretrofit.di.profile;

import com.abdelrahmman.daggerretrofit.network.profile.change.ChangePasswordApi;
import com.abdelrahmman.daggerretrofit.network.profile.delete.DeleteAccountApi;
import com.abdelrahmman.daggerretrofit.network.profile.update.UpdateProfileApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ProfileModule {

    @ProfileScope
    @Provides
    static UpdateProfileApi provideUpdateProfileApi(Retrofit retrofit){
        return retrofit.create(UpdateProfileApi.class);
    }

    @ProfileScope
    @Provides
    static ChangePasswordApi provideChangePasswordApi(Retrofit retrofit){
        return retrofit.create(ChangePasswordApi.class);
    }

    @ProfileScope
    @Provides
    static DeleteAccountApi provideDeleteAccountApi(Retrofit retrofit){
        return retrofit.create(DeleteAccountApi.class);
    }

}
