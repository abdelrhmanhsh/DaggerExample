package com.abdelrahmman.daggerretrofit.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LoginResource<T> {

    @NonNull
    public final AuthStatus status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;


    public LoginResource(@NonNull AuthStatus status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> LoginResource<T> authenticated (@NonNull String msg, @Nullable T data) {
        return new LoginResource<>(AuthStatus.AUTHENTICATED, data, msg);
    }

    public static <T> LoginResource<T> error(@NonNull String msg, @Nullable T data) {
        return new LoginResource<>(AuthStatus.ERROR, data, msg);
    }

    public static <T> LoginResource<T> loading(@Nullable T data) {
        return new LoginResource<>(AuthStatus.LOADING, data, null);
    }

    public static <T> LoginResource<T> logout () {
        return new LoginResource<>(AuthStatus.NOT_AUTHENTICATED, null, null);
    }

    public enum AuthStatus { AUTHENTICATED, ERROR, LOADING, NOT_AUTHENTICATED}

}
