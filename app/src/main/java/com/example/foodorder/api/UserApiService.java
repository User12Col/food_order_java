package com.example.foodorder.api;

import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.User;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserApiService {

    //http://localhost:8082/api/v1/Users/email=kiet@gmail.com&password=pass123456
    String baseUrl = "http://192.168.1.146:8082/api/v1/";
    UserApiService userApiService = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService.class);


    @GET("Users/login")
    Observable<ResponeObject> login(@Query("email") String email, @Query("password") String password);

    @POST("Users/signup")
    Observable<ResponeObject> signUp(@Body User user);

    @PUT("Users/updateUser")
    Observable<ResponeObject> updateUser(@Query("id") String id, @Body User user);
}
