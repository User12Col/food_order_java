package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.adapter.CartAdapter;
import com.example.foodorder.api.CartApiService;
import com.example.foodorder.helper.DialogHelper;
import com.example.foodorder.models.Cart;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CartScreen extends AppCompatActivity {

    private Button buttonOrder;
    private RecyclerView rclCart;
    private TextView txtTotalPrice;
    private ImageView imgBack;
    private Dialog dialog;

    List<Cart> carts;
    int totalPrice =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);

        reference();

        User user = DataLocalManager.getUser();
        callApi(user.getUserID());
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper dialogHelper = new DialogHelper(CartScreen.this, dialog);
                dialogHelper.showLoadingDialog();
                if(user.getAddress().isEmpty() || user.getPhone().isEmpty()){
                    Toast.makeText(CartScreen.this, "Cập nhật địa chỉ và số điện thoại trước khi mua hàng", Toast.LENGTH_LONG).show();
                } else if(carts.size() == 0){
                    Toast.makeText(CartScreen.this, "Chọn món ăn trước khi mua hàng", Toast.LENGTH_LONG).show();
                } else{
                    Intent intent = new Intent(CartScreen.this, PayScreen.class);
                    startActivity(intent);
                }

                dialogHelper.dismissLoadingDialog();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void reference(){
        rclCart = findViewById(R.id.rclCart);
        buttonOrder = findViewById(R.id.btnOrder);

        txtTotalPrice = findViewById(R.id.txtTotalPriceCart);

        imgBack = findViewById(R.id.imgBack);
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
                        Toast.makeText(CartScreen.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(CartScreen.this, LinearLayoutManager.VERTICAL, false);
                        CartAdapter cartAdapter = new CartAdapter(CartScreen.this, carts, txtTotalPrice);
                        rclCart.setLayoutManager(verticalLayoutManagaer);
                        rclCart.setAdapter(cartAdapter);

//                        totalPrice = calTotalPrice(carts);
//                        txtTotalPrice.setText(String.valueOf(totalPrice));
                    }
                });
    }
}
