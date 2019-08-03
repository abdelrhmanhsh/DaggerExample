package com.abdelrahmman.daggerretrofit.di.register;

import androidx.lifecycle.ViewModel;

import com.abdelrahmman.daggerretrofit.di.ViewModelKey;
import com.abdelrahmman.daggerretrofit.ui.register.RegisterViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RegisterViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    public abstract ViewModel bindRegisterViewModel(RegisterViewModel viewModel);

}
