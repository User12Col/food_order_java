package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.adapter.CartAdapter;
import com.example.foodorder.api.CartApiService;
import com.example.foodorder.api.NotificationApiService;
import com.example.foodorder.api.OrderApiService;
import com.example.foodorder.api.OrderDetailApiService;
import com.example.foodorder.helper.DialogHelper;
import com.example.foodorder.helper.Format;
import com.example.foodorder.models.Cart;
import com.example.foodorder.models.Notification;
import com.example.foodorder.models.Order;
import com.example.foodorder.models.OrderDetail;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.Role;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PayScreen extends AppCompatActivity {

    private Button buttonPay;

    private TextView txtOrderAddress, txtOrderPrice;

    private RecyclerView rclOrderFood;

    private ImageView imgBackToCart;

    private Dialog dialog;
    List<Cart> carts;
    int totalPrice =0;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_screen);

        reference();

        User user = DataLocalManager.getUser();

        txtOrderAddress.setText(user.getAddress());
        callApi(user.getUserID());

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper dialogHelper = new DialogHelper(PayScreen.this, dialog);

                String orderID = UUID.randomUUID().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                Date currentDate = new Date();
                String currentDateStr = dateFormat.format(currentDate);
                totalPrice = calTotalPrice(carts);
                User employee = new User("vbqfniwcncqincqi", "admin", "TPHCM", "0909090909", "admin@gmail.com", "admin", new Role(0, "admin"));

                Order order = new Order(orderID, currentDateStr, "Preparing", user.getAddress(), totalPrice, user, employee);
                callApiAddOrder(order, dialogHelper);

                dialogHelper.showLoadingDialog();
                for(Cart cart : carts){
                    System.out.println(order.getOrderID());
                    System.out.println(cart.getFood().getFoodID());
                    System.out.println(cart.getQuantity()+"");
                    OrderDetail orderDetail = new OrderDetail(order, cart.getFood(), cart.getQuantity());
                    System.out.println(orderDetail.getOrderDetailID());
                    callApiAddOrderDetail(orderDetail);
                }

                Notification notification = new Notification(currentDateStr, "Đơn hàng "+orderID+" được đặt thành công", user);
                callApiAddNotification(notification);

                callApiDeleteCart(user.getUserID());

                Intent intent = new Intent(PayScreen.this, ResultActivity.class);
                startActivity(intent);
                dialogHelper.dismissLoadingDialog();
            }
        });
    }


    private void reference(){
        buttonPay = findViewById(R.id.btnPay);

        txtOrderAddress = findViewById(R.id.txtOrderAddress);
        txtOrderPrice = findViewById(R.id.txtOrderPrice);

        rclOrderFood = findViewById(R.id.rclOrderFood);

//        imgBackToCart = findViewById(R.id.imgBackToCart);
    }

    private int calTotalPrice(List<Cart> carts){
        int total = 0;
        for (Cart cart : carts) {
            total = total + (int)cart.getTotalPrice();
        }
        return total;
    }

    private void callApi(String userID){
        CartApiService.cartApiService.getCartByID(userID)
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
                            Type listType = new TypeToken<List<Cart>>() {}.getType();
                            carts = gson.fromJson(gson.toJson(data), listType);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(PayScreen.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(PayScreen.this, LinearLayoutManager.VERTICAL, false);
                        CartAdapter cartAdapter = new CartAdapter(PayScreen.this, carts, txtOrderPrice);
                        rclOrderFood.setLayoutManager(verticalLayoutManager);
                        rclOrderFood.setAdapter(cartAdapter);
                    }
                });
    }

    private void callApiAddOrder(Order order, DialogHelper dialogHelper){
        dialogHelper.showLoadingDialog();
        OrderApiService.orderApiService.addOrder(order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponeObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponeObject responeObject) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(PayScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        dialogHelper.dismissLoadingDialog();
                    }
                });

    }

    private void callApiAddOrderDetail(OrderDetail orderDetail){
        OrderDetailApiService.orderDetailApiService.addOrderDetail(orderDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponeObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponeObject responeObject) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Add order detail");
                        message = "Ok";
                    }
                });
    }

    private void callApiAddNotification(Notification notification){
        NotificationApiService.notificationApiService.addNotification(notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponeObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponeObject responeObject) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
//                        android.app.Notification noti = new android.app.Notification.Builder(PayScreen.this)
//                                .setContentTitle("Đặt món thành công")
//                                .setContentText(notification.getContent())
//                                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                                .build();
//
//                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                        if(notificationManager != null){
//                            notificationManager.notify(1, noti);
//                        }

                        String channelID = "CHANNEL_ID";
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
                        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("Đặt món thành công")
                                .setContentText(notification.getContent())
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
                            if(notificationChannel == null){
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                notificationChannel = new NotificationChannel(channelID, "Android", importance);
                                notificationChannel.setLightColor(Color.BLUE);
                                notificationChannel.enableVibration(true);
                                notificationManager.createNotificationChannel(notificationChannel);
                            }
                        }
                        notificationManager.notify(0, builder.build());
                    }
                });

    }

    private void callApiDeleteCart(String userID){
        CartApiService.cartApiService.deleteUserCart(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponeObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponeObject responeObject) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Delete ok");
                    }
                });
    }
}