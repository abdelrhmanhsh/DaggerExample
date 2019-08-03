package com.abdelrahmman.daggerretrofit.di.login;

import androidx.lifecycle.ViewModel;

import com.abdelrahmman.daggerretrofit.di.ViewModelKey;
import com.abdelrahmman.daggerretrofit.ui.login.LoginViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class LoginViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    public abstract ViewModel bindLoginViewModel(LoginViewModel viewModel);

}
