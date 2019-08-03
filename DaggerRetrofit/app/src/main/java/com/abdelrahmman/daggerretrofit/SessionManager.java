package com.abdelrahmman.daggerretrofit;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.abdelrahmman.daggerretrofit.models.LoginResponse;
import com.abdelrahmman.daggerretrofit.ui.login.LoginResource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {

    private static final String TAG = "SessionManager";

    private MediatorLiveData<LoginResource<LoginResponse>> cachedUser = new MediatorLiveData<>();

    @Inject
    public SessionManager(){
    }

    public void authenticateUser(final LiveData<LoginResource<LoginResponse>> source){
        if (cachedUser != null){
            cachedUser.setValue(LoginResource.loading((LoginResponse)null));
            cachedUser.addSource(source, new Observer<LoginResource<LoginResponse>>() {
                @Override
                public void onChanged(LoginResource<LoginResponse> user) {
                    cachedUser.setValue(user);
                    cachedUser.removeSource(source);
                }
            });
        }
    }
    public void logOut(){
        Log.d(TAG, "logOut: logging out");
        cachedUser.setValue(LoginResource.<LoginResponse>logout());
    }

    public LiveData<LoginResource<LoginResponse>> getAuthUser(){
        return cachedUser;
    }
}
