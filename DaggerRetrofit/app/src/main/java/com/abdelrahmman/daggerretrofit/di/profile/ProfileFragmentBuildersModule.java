package com.abdelrahmman.daggerretrofit.di.profile;

import com.abdelrahmman.daggerretrofit.ui.profile.change.ChangePasswordFragment;
import com.abdelrahmman.daggerretrofit.ui.profile.delete.DeleteAccountFragment;
import com.abdelrahmman.daggerretrofit.ui.profile.update.UpdateProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ProfileFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract UpdateProfileFragment contributeUpdateProfileFragment();

    @ContributesAndroidInjector
    abstract ChangePasswordFragment contributeChangePasswordFragment();

    @ContributesAndroidInjector
    abstract DeleteAccountFragment contributeDeleteAccountFragment();

}

