package com.abdelrahmman.daggerretrofit.ui.profile.change;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.abdelrahmman.daggerretrofit.SessionManager;
import com.abdelrahmman.daggerretrofit.models.DefaultResponse;
import com.abdelrahmman.daggerretrofit.models.LoginResponse;
import com.abdelrahmman.daggerretrofit.network.profile.change.ChangePasswordApi;
import com.abdelrahmman.daggerretrofit.ui.login.LoginResource;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ChangePasswordViewModel extends ViewModel {

    //inject
    private ChangePasswordApi changePasswordApi;

    private SessionManager sessionManager;
    public MediatorLiveData<LoginResource<DefaultResponse>> updatedUser = new MediatorLiveData<>();

    @Inject
    public ChangePasswordViewModel(ChangePasswordApi changePasswordApi, SessionManager sessionManager) {
        this.changePasswordApi = changePasswordApi;
        this.sessionManager = sessionManager;
        Log.d(TAG, "ChangePasswordViewModel: viewmodel is working");
    }

    public void UpdateUser(String current_password, String new_password, String email){
        Log.d(TAG, "updateUser: attempting to change password");
        updateUserSource(updateUser(current_password, new_password, email));
    }

    private LiveData<LoginResource<DefaultResponse>> updateUser(String current_password, String new_password, String email){

        return LiveDataReactiveStreams.fromPublisher(
                changePasswordApi.changePassword(current_password, new_password, email)

                        .onErrorReturn(new Function<Throwable, DefaultResponse>() {
                            @Override
                            public DefaultResponse apply(Throwable throwable) throws Exception {

                                DefaultResponse errorUser = new DefaultResponse();
                                errorUser.setError(true);

                                return errorUser;
                            }
                        })

                        .map(new Function<DefaultResponse, LoginResource<DefaultResponse>>() {
                            @Override
                            public LoginResource<DefaultResponse> apply(DefaultResponse user) throws Exception {

                                if (user.isError()){
                                    return LoginResource.error("Couldn't Change Your Password", (DefaultResponse)null);
                                }

                                return LoginResource.authenticated("Password Changed Successfully",user);
                            }
                        })

                        .subscribeOn(Schedulers.io()));
    }

    public void updateUserSource(final LiveData<LoginResource<DefaultResponse>> source){
        if (updatedUser != null){
            updatedUser.setValue(LoginResource.loading((DefaultResponse)null));
            updatedUser.addSource(source, new Observer<LoginResource<DefaultResponse>>() {
                @Override
                public void onChanged(LoginResource<DefaultResponse> user) {
                    updatedUser.setValue(user);
                    updatedUser.removeSource(source);
                }
            });
        }
    }

    public LiveData<LoginResource<LoginResponse>> getAuthenticatedUser(){
        return sessionManager.getAuthUser();
    }
}
