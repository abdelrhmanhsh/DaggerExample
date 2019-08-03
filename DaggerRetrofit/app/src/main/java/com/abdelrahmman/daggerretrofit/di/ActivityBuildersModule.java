package com.abdelrahmman.daggerretrofit.di;

import com.abdelrahmman.daggerretrofit.di.login.LoginModule;
import com.abdelrahmman.daggerretrofit.di.login.LoginScope;
import com.abdelrahmman.daggerretrofit.di.login.LoginViewModelsModule;
import com.abdelrahmman.daggerretrofit.di.main.MainScope;
import com.abdelrahmman.daggerretrofit.di.main.MainViewModelsModule;
import com.abdelrahmman.daggerretrofit.di.profile.ProfileFragmentBuildersModule;
import com.abdelrahmman.daggerretrofit.di.profile.ProfileModule;
import com.abdelrahmman.daggerretrofit.di.profile.ProfileScope;
import com.abdelrahmman.daggerretrofit.di.profile.ProfileViewModelsModule;
import com.abdelrahmman.daggerretrofit.di.register.RegisterModule;
import com.abdelrahmman.daggerretrofit.di.register.RegisterScope;
import com.abdelrahmman.daggerretrofit.di.register.RegisterViewModelsModule;
import com.abdelrahmman.daggerretrofit.ui.login.LoginActivity;
import com.abdelrahmman.daggerretrofit.ui.main.MainActivity;
import com.abdelrahmman.daggerretrofit.ui.profile.ProfileSettingsActivity;
import com.abdelrahmman.daggerretrofit.ui.register.RegisterActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @LoginScope
    @ContributesAndroidInjector(
            modules = {LoginViewModelsModule.class, LoginModule.class})
    abstract LoginActivity contributeAuthActivity();

    @RegisterScope
    @ContributesAndroidInjector(
            modules = {RegisterViewModelsModule.class, RegisterModule.class})
    abstract RegisterActivity contributeRegisterActivity();

    @MainScope
    @ContributesAndroidInjector(
            modules = {MainViewModelsModule.class}
    )
    abstract MainActivity contributeMainActivity();

    @ProfileScope
    @ContributesAndroidInjector(
            modules = {ProfileFragmentBuildersModule.class, ProfileViewModelsModule.class, ProfileModule.class}
    )
    abstract ProfileSettingsActivity contributeProfileSettingsActivity();

}
