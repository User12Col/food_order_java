package com.example.foodorder.api;

import com.example.foodorder.models.Notification;
import com.example.foodorder.models.ResponeObject;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationApiService {
    String baseUrl = "http://172.20.10.4:8082/api/v1/";

    NotificationApiService notificationApiService= new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotificationApiService.class);

    @GET("Notifications/getNotificationByUserID")
    Observable<ResponeObject> getNotificationByUserID(@Query("userID") String userID);

    @POST("Notifications/addNotification")
    Observable<ResponeObject> addNotification(@Body Notification notification);
}
