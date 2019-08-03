package com.abdelrahmman.daggerretrofit.ui.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.abdelrahmman.daggerretrofit.SessionManager;
import com.abdelrahmman.daggerretrofit.models.LoginResponse;
import com.abdelrahmman.daggerretrofit.network.login.LoginApi;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {


    private static final String TAG = "AuthViewModel";

    //inject
    private final LoginApi loginApi;

    private SessionManager sessionManager;

    @Inject
    public LoginViewModel(LoginApi loginApi, SessionManager sessionManager){
        this.loginApi = loginApi;
        this.sessionManager = sessionManager;
        Log.d(TAG, "LoginViewModel: viewModel is working...");

    }

    public void authenticateUser(String email, String password){
        Log.d(TAG, "queryUser: attempting to login");
        sessionManager.authenticateUser(queryUser(email, password));
    }

    private LiveData<LoginResource<LoginResponse>> queryUser(String email, String password){

        return LiveDataReactiveStreams.fromPublisher(
                loginApi.userLogin(email, password)

                        .onErrorReturn(new Function<Throwable, LoginResponse>() {
                            @Override
                            public LoginResponse apply(Throwable throwable) throws Exception {

                                LoginResponse errorUser = new LoginResponse();
                                errorUser.setError(true);

                                return errorUser;
                            }
                        })

                        .map(new Function<LoginResponse, LoginResource<LoginResponse>>() {
                            @Override
                            public LoginResource<LoginResponse> apply(LoginResponse user) throws Exception {

                                if (user.isError()){
                                    return LoginResource.error("Couldn't Authenticate", (LoginResponse)null);
                                }

                                return LoginResource.authenticated("Login Successfully", user);
                            }
                        })

                        .subscribeOn(Schedulers.io()));
    }

    public LiveData<LoginResource<LoginResponse>> observeAuthUser(){
        return sessionManager.getAuthUser();
    }

}
