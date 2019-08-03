package com.abdelrahmman.daggerretrofit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.abdelrahmman.daggerretrofit.models.LoginResponse;
import com.abdelrahmman.daggerretrofit.ui.login.LoginActivity;
import com.abdelrahmman.daggerretrofit.ui.login.LoginResource;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class BaseFragment extends DaggerFragment {

    private static final String TAG = "BaseFragment";

    @Inject
    public SessionManager sessionManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        subscribeObservers();

    }

    private void subscribeObservers(){
        sessionManager.getAuthUser().observe(this, new Observer<LoginResource<LoginResponse>>() {
            @Override
            public void onChanged(LoginResource<LoginResponse> userAuthResource) {
                if(userAuthResource != null){
                    switch (userAuthResource.status){
                        case LOADING: {
                            break;
                        }
                        case AUTHENTICATED:{
                            Log.d(TAG, "onChanged: Login success: " + userAuthResource.data.getUser().getEmail());
                            break;
                        }

                        case ERROR: {
                            break;
                        }

                        case NOT_AUTHENTICATED: {
                            navLoginScreen();
                            break;
                        }
                    }
                }
            }
        });
    }

    private void navLoginScreen(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
