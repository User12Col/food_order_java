package com.example.foodorder.api;

import com.example.foodorder.models.OrderDetail;
import com.example.foodorder.models.ResponeObject;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderDetailApiService {
    String baseUrl = "http://192.168.1.146:8082/api/v1/";

    OrderDetailApiService orderDetailApiService = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderDetailApiService.class);

    @GET("OrderDetails/getByOrderID")
    Observable<ResponeObject> getByOrderID(@Query("orderID") String orderID);

    @POST("OrderDetails/addOrderDetail")
    Observable<ResponeObject> addOrderDetail(@Body OrderDetail orderDetail);
}
