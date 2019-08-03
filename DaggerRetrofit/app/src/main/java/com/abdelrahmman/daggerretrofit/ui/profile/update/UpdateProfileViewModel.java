package com.abdelrahmman.daggerretrofit.ui.profile.update;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.abdelrahmman.daggerretrofit.SessionManager;
import com.abdelrahmman.daggerretrofit.models.LoginResponse;
import com.abdelrahmman.daggerretrofit.network.profile.update.UpdateProfileApi;
import com.abdelrahmman.daggerretrofit.ui.login.LoginResource;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UpdateProfileViewModel extends ViewModel {

    //inject
    private UpdateProfileApi updateProfileApi;
    private SessionManager sessionManager;

    @Inject
    public UpdateProfileViewModel(UpdateProfileApi updateProfileApi, SessionManager sessionManager) {
        this.updateProfileApi = updateProfileApi;
        this.sessionManager = sessionManager;
        Log.d(TAG, "UpdateProfileViewModel: viewModel is working...");
    }

    public void UpdateUser(int id, String email, String name, String school){
        Log.d(TAG, "updateUser: attempting to update user settings");
        sessionManager.authenticateUser(updateUser(id, email, name, school));
    }

    private LiveData<LoginResource<LoginResponse>> updateUser(int id, String email, String name, String school){

        return LiveDataReactiveStreams.fromPublisher(
                updateProfileApi.updateUser(id, email, name, school)

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

                                return LoginResource.authenticated("Updated Successfully", user);
                            }
                        })

                        .subscribeOn(Schedulers.io()));
    }

    public LiveData<LoginResource<LoginResponse>> getAuthenticatedUser(){
        return sessionManager.getAuthUser();
    }

}
