package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.adapter.CartAdapter;
import com.example.foodorder.api.CartApiService;
import com.example.foodorder.models.Cart;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.User;
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

public class CartScreen extends AppCompatActivity {

    private Button buttonOrder;
    private RecyclerView rclCart;
    private TextView txtTotalPrice;
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
                Intent intent = new Intent(CartScreen.this, PayScreen.class);
                startActivity(intent);
            }
        });
    }

    public void goBackToMainActivity(View view) {
        // Tạo một Intent để quay về MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void reference(){
        rclCart = findViewById(R.id.rclCart);
        buttonOrder = findViewById(R.id.button_order);

        txtTotalPrice = findViewById(R.id.txtTotalPriceCart);
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
                        Toast.makeText(CartScreen.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(CartScreen.this, LinearLayoutManager.VERTICAL, false);
                        CartAdapter cartAdapter = new CartAdapter(CartScreen.this, carts);
                        rclCart.setLayoutManager(verticalLayoutManagaer);
                        rclCart.setAdapter(cartAdapter);

                        totalPrice = calTotalPrice(carts);
                        txtTotalPrice.setText(String.valueOf(totalPrice));
                    }
                });
    }
}
