package com.abdelrahmman.daggerretrofit.utils;

import android.util.Base64;

public class Constants {

    public static final String BASE_URL = "http://domain/MyApi/public/";
    public static final String AUTH = "Basic " + Base64.encodeToString(("username:password").getBytes(), Base64.NO_WRAP);

}
