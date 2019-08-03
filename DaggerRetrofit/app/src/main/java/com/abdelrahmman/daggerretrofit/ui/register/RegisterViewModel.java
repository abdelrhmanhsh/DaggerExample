package com.abdelrahmman.daggerretrofit.ui.register;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.abdelrahmman.daggerretrofit.models.RegisterResponse;
import com.abdelrahmman.daggerretrofit.network.register.RegisterApi;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterViewModel extends ViewModel {

    //inject
    private final RegisterApi registerApi;

    private MediatorLiveData<RegisterResource<RegisterResponse>> registeredUser = new MediatorLiveData<>();

    @Inject
    public RegisterViewModel(RegisterApi registerApi) {
        this.registerApi = registerApi;
    }

    public void RegisterUser(String email, String password, String name, String school){
        Log.d(TAG, "queryUser: attempting to register");
        createUser(createUser(email, password, name, school));
    }

    public LiveData<RegisterResource<RegisterResponse>> createUser(String email, String password, String name, String school){
        return LiveDataReactiveStreams.fromPublisher(
                registerApi.createUser(email, password, name, school)

                        .onErrorReturn(new Function<Throwable, RegisterResponse>() {
                            @Override
                            public RegisterResponse apply(Throwable throwable) throws Exception {

                                RegisterResponse errorUser = new RegisterResponse();
                                errorUser.setError(true);

                                return errorUser;
                            }
                        })

                        .map(new Function<RegisterResponse, RegisterResource<RegisterResponse>>() {
                            @Override
                            public RegisterResource<RegisterResponse> apply(RegisterResponse user) throws Exception {

                                if (user.isError()){
                                    return RegisterResource.error("Couldn't Register", (RegisterResponse)null);
                                }

                                return RegisterResource.registered("Registered Successfully", user);
                            }
                        })

                        .subscribeOn(Schedulers.io()));

    }

    public void createUser(final LiveData<RegisterResource<RegisterResponse>> source){
        if (registeredUser != null){
            registeredUser.setValue(RegisterResource.loading((RegisterResponse)null));
            registeredUser.addSource(source, new Observer<RegisterResource<RegisterResponse>>() {
                @Override
                public void onChanged(RegisterResource<RegisterResponse> user) {
                    registeredUser.setValue(user);
                    registeredUser.removeSource(source);
                }
            });
        }
    }

    public LiveData<RegisterResource<RegisterResponse>> observeCreatedUser(){
        return registeredUser;
    }

}
