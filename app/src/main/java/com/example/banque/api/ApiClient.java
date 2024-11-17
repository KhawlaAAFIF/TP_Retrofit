package com.example.banque.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8082";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(String acceptHeader) {
        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL);

            if ("application/xml".equals(acceptHeader)) {
                builder.addConverterFactory(SimpleXmlConverterFactory.create());
            } else {
                builder.addConverterFactory(GsonConverterFactory.create());
            }

            retrofit = builder.build();
        }
        return retrofit;
    }
}

