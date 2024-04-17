package com.example.foodorder.api;

import com.example.foodorder.models.ResponeObject;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface CategoryApiService {
    String baseUrl = "http://192.168.1.146:8082/api/v1/";

    CategoryApiService categoryApiService = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoryApiService.class);

    @GET("Categories")
    Observable<ResponeObject> getAllCategory();
}
