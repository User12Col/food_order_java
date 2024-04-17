package com.example.foodorder.api;

import com.example.foodorder.models.Order;
import com.example.foodorder.models.ResponeObject;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderApiService {
    String baseUrl = "http://192.168.1.146:8082/api/v1/";

    OrderApiService orderApiService = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderApiService.class);

    @GET("Orders/getOrderByUserID")
    Observable<ResponeObject> getOrderByUserID(@Query("userID") String userID);

    @GET("Orders/getOrderByUserAndStatus")
    Observable<ResponeObject> getOrderByUserAndStatus(@Query("userID") String userID, @Query("status") String status);

    @POST("Orders/addOrder")
    Observable<ResponeObject> addOrder(@Body Order order);
}
