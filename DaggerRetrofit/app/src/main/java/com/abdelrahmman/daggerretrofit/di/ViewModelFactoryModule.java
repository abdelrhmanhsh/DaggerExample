package com.abdelrahmman.daggerretrofit.di;

import androidx.lifecycle.ViewModelProvider;

import com.abdelrahmman.daggerretrofit.viewmodel.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory modelProviderFactory);

}
