package com.abdelrahmman.daggerretrofit.ui.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RegisterResource<T> {

    @NonNull
    public final AuthStatus status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;


    public RegisterResource(@NonNull AuthStatus status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> RegisterResource<T> registered (@NonNull String msg, @Nullable T data) {
        return new RegisterResource<>(AuthStatus.REGISTERED, data, msg);
    }

    public static <T> RegisterResource<T> error(@NonNull String msg, @Nullable T data) {
        return new RegisterResource<>(AuthStatus.ERROR, data, msg);
    }

    public static <T> RegisterResource<T> loading(@Nullable T data) {
        return new RegisterResource<>(AuthStatus.LOADING, data, null);
    }

    public static <T> RegisterResource<T> logout () {
        return new RegisterResource<>(AuthStatus.NOT_AUTHENTICATED, null, null);
    }

    public enum AuthStatus { REGISTERED, ERROR, LOADING, NOT_AUTHENTICATED}

}
