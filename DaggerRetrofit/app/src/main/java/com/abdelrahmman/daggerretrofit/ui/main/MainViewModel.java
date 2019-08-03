package com.abdelrahmman.daggerretrofit.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.abdelrahmman.daggerretrofit.SessionManager;
import com.abdelrahmman.daggerretrofit.models.LoginResponse;
import com.abdelrahmman.daggerretrofit.ui.login.LoginResource;

import javax.inject.Inject;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainViewModel extends ViewModel {

    private final SessionManager sessionManager;

    @Inject
    public MainViewModel(SessionManager sessionManager){
        this.sessionManager = sessionManager;
        Log.d(TAG, "mainViewModel: viewModel is ready");
    }

    public LiveData<LoginResource<LoginResponse>> getAuthenticatedUser(){
        return sessionManager.getAuthUser();
    }

}
