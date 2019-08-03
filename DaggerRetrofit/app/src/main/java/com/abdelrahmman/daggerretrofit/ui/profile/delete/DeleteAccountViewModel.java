package com.abdelrahmman.daggerretrofit.ui.profile.delete;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.abdelrahmman.daggerretrofit.SessionManager;
import com.abdelrahmman.daggerretrofit.models.DefaultResponse;
import com.abdelrahmman.daggerretrofit.models.LoginResponse;
import com.abdelrahmman.daggerretrofit.network.profile.delete.DeleteAccountApi;
import com.abdelrahmman.daggerretrofit.ui.login.LoginResource;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DeleteAccountViewModel extends ViewModel {

    //inject
    private DeleteAccountApi deleteAccountApi;
    private final SessionManager sessionManager;

    public MediatorLiveData<LoginResource<DefaultResponse>> deletedUser = new MediatorLiveData<>();

    @Inject
    public DeleteAccountViewModel(DeleteAccountApi deleteAccountApi, SessionManager sessionManager) {
        this.deleteAccountApi = deleteAccountApi;
        this.sessionManager = sessionManager;
        Log.d(TAG, "DeleteAccountViewModel: viewModel is working");
    }

    public void DeleteUser(int id){
        Log.d(TAG, "updateUser: attempting delete");
        deleteUserSource(deleteUser(id));
    }

    private LiveData<LoginResource<DefaultResponse>> deleteUser(int id){

        return LiveDataReactiveStreams.fromPublisher(
                deleteAccountApi.deleteUser(id)

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
                                    return LoginResource.error("Couldn't delete Account!", (DefaultResponse)null);
                                }

                                return LoginResource.authenticated("Account Deleted Successfully", user);
                            }
                        })

                        .subscribeOn(Schedulers.io()));
    }

    public void deleteUserSource(final LiveData<LoginResource<DefaultResponse>> source){
        if (deletedUser != null){
            deletedUser.setValue(LoginResource.loading((DefaultResponse)null));
            deletedUser.addSource(source, new Observer<LoginResource<DefaultResponse>>() {
                @Override
                public void onChanged(LoginResource<DefaultResponse> user) {
                    deletedUser.setValue(user);
                    deletedUser.removeSource(source);
                }
            });
        }
    }

    public LiveData<LoginResource<LoginResponse>> getAuthenticatedUser(){
        return sessionManager.getAuthUser();
    }

}