package com.example.mywallpaper.service;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.mywallpaper.service.URLContaints.BASE_URL_IMAGE;

public class ImageInstance {
    public static Retrofit retrofit;
    public static Retrofit getInstance(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_IMAGE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
