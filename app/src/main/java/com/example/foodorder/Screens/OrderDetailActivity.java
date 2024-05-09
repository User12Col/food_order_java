package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.foodorder.R;
import com.example.foodorder.adapter.OrderDetailAdapter;
import com.example.foodorder.api.OrderDetailApiService;
import com.example.foodorder.helper.DialogHelper;
import com.example.foodorder.helper.Format;
import com.example.foodorder.models.Cart;
import com.example.foodorder.models.OrderDetail;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView txtUserName, txtAddress, txtUserEmail, txtUserPhone, txtOrderID, txtOrderTotalPrice, txtPayPrice;
    private RecyclerView rclOrderDetail;
    private List<OrderDetail> orderDetails;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        reference();

        Intent intent = getIntent();
        String orderID = intent.getStringExtra("orderID");
        DialogHelper dialogHelper = new DialogHelper(OrderDetailActivity.this, dialog);
        dialogHelper.showLoadingDialog();
        callApi(orderID);
        dialogHelper.dismissLoadingDialog();

    }

    private void reference(){
        txtUserName = findViewById(R.id.txtUserName);
        txtAddress = findViewById(R.id.txtAddress);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtUserPhone = findViewById(R.id.txtUserPhone);
        txtOrderID = findViewById(R.id.txtOrderID);
        txtOrderTotalPrice = findViewById(R.id.txtOrderTotalPrice);
        txtPayPrice = findViewById(R.id.txtPayPrice);

        rclOrderDetail = findViewById(R.id.rclOrderDetail);
    }

    private void callApi(String orderID){
        OrderDetailApiService.orderDetailApiService.getByOrderID(orderID)
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
                            Type listType = new TypeToken<List<OrderDetail>>() {}.getType();
                            orderDetails = gson.fromJson(gson.toJson(data), listType);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        User user = orderDetails.get(0).getOrder().getUser();
                        txtUserName.setText(user.getName());
                        txtAddress.setText(orderDetails.get(0).getOrder().getAddress());
                        txtUserEmail.setText(user.getEmail());
                        txtUserPhone.setText(user.getPhone());
                        txtOrderID.setText(orderDetails.get(0).getOrder().getOrderID());
                        txtOrderTotalPrice.setText(Format.formatCurrency(orderDetails.get(0).getOrder().getTotalPrice()));
                        txtPayPrice.setText(Format.formatCurrency(orderDetails.get(0).getOrder().getTotalPrice()));

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(OrderDetailActivity.this, orderDetails);
                        rclOrderDetail.setLayoutManager(linearLayoutManager);
                        rclOrderDetail.setAdapter(orderDetailAdapter);
                    }
                });
    }
}