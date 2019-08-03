package com.abdelrahmman.daggerretrofit.di.profile;

import androidx.lifecycle.ViewModel;

import com.abdelrahmman.daggerretrofit.di.ViewModelKey;
import com.abdelrahmman.daggerretrofit.ui.profile.change.ChangePasswordViewModel;
import com.abdelrahmman.daggerretrofit.ui.profile.delete.DeleteAccountViewModel;
import com.abdelrahmman.daggerretrofit.ui.profile.update.UpdateProfileViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ProfileViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(UpdateProfileViewModel.class)
    public abstract ViewModel bindUpdateProfileViewModel(UpdateProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel.class)
    public abstract ViewModel bindChangePasswordViewModel(ChangePasswordViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DeleteAccountViewModel.class)
    public abstract ViewModel bindDeleteAccountViewModel(DeleteAccountViewModel viewModel);

}
