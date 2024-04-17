package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.adapter.CartAdapter;
import com.example.foodorder.adapter.OrderAdapter;
import com.example.foodorder.api.OrderApiService;
import com.example.foodorder.helper.DialogHelper;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.Order;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.storage.DataLocalManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DeliveryScreen extends AppCompatActivity {
    private RecyclerView rclOrder;
    private LinearLayout itemDelivering, itemDelivered, itemPreparing;
    private ImageView imgBack;

    List<Order> orders;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_screen);
        reference();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        callApi("Preparing");

        itemDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi("Delivered");
            }
        });

        itemPreparing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi("Preparing");
            }
        });

        itemDelivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi("Delivering");
            }
        });

    }

    private void callApi(String status){
        DialogHelper dialogHelper = new DialogHelper(DeliveryScreen.this, dialog);
        dialogHelper.showLoadingDialog();
        String userID = DataLocalManager.getUser().getUserID();
        OrderApiService.orderApiService.getOrderByUserAndStatus(userID, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponeObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponeObject responeObject) {
                        Object data = responeObject.getData();
                        if(data instanceof List<?>){
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Order>>() {}.getType();
                            orders = gson.fromJson(gson.toJson(data), listType);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(DeliveryScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(DeliveryScreen.this, LinearLayoutManager.VERTICAL, false);
                        OrderAdapter orderAdapter = new OrderAdapter(DeliveryScreen.this, orders);
                        rclOrder.setLayoutManager(verticalLayoutManager);
                        rclOrder.setAdapter(orderAdapter);
                        dialogHelper.dismissLoadingDialog();
                    }
                });
    }

    private void reference(){
        rclOrder = findViewById(R.id.rclOrder);

        itemDelivered = findViewById(R.id.itemDelivered);
        itemDelivering = findViewById(R.id.itemDelivering);
        itemPreparing = findViewById(R.id.itemPreparing);

        imgBack = findViewById(R.id.imgBackOfOrder);
    }
}