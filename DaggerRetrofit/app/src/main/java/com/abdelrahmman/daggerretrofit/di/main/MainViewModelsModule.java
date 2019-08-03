package com.abdelrahmman.daggerretrofit.di.main;

import androidx.lifecycle.ViewModel;

import com.abdelrahmman.daggerretrofit.di.ViewModelKey;
import com.abdelrahmman.daggerretrofit.ui.main.MainViewModel;
import com.abdelrahmman.daggerretrofit.ui.register.RegisterViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindRegisterViewModel(MainViewModel viewModel);

}
