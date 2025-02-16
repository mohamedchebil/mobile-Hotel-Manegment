package com.example.projectapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    private static  final String BASE_URL = "http://192.168.56.1:8085/api/";
   public static Retrofit getRetrofitInstance() {
       if (retrofit == null) {
           retrofit = new Retrofit.Builder()
                   .baseUrl(BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();



       }

       return retrofit;
   }}