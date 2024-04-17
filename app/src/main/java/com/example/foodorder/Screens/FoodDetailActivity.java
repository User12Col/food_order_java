package com.example.foodorder.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.api.CartApiService;
import com.example.foodorder.models.Cart;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FoodDetailActivity extends AppCompatActivity {

    private ImageView imgFoodDetail;
    private TextView txtFoodNameDetail, txtFoodDescribeDetail, txtFoodPriceDetail, txtFoodDetailQuantity;
    private Button btnFoodDetailIncrease, btnFoodDetailDecrease;

    double quantity = 0;
    double price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        reference();

        Intent intent = getIntent();
        Gson gson = new Gson();
        Food food = gson.fromJson(intent.getStringExtra("food"), Food.class);

        Picasso.get().load(food.getImage()).into(imgFoodDetail);
        txtFoodNameDetail.setText(food.getName());
        txtFoodDescribeDetail.setText(food.getDescription());
        txtFoodPriceDetail.setText(String.valueOf(food.getUnitPrice()));

        User user = DataLocalManager.getUser();

        CartApiService.cartApiService.getQuantity(food.getFoodID(), user.getUserID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponeObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponeObject responeObject) {
                        Object data = responeObject.getData();
                        if(data instanceof Double){
                            quantity = (Double) data;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        txtFoodDetailQuantity.setText(String.valueOf((int)quantity));
                    }
                });

        btnFoodDetailIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart cart = new Cart(food, user, 1, food.getUnitPrice());
                CartApiService.cartApiService.addToCart(cart)
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
                                Toast.makeText(FoodDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                                quantity = quantity + 1.0;
                                txtFoodDetailQuantity.setText(String.valueOf((int)quantity));
                            }
                        });
            }
        });

        btnFoodDetailDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity > 1){
                    Cart cart = new Cart(food, user, 1, food.getUnitPrice());
                    CartApiService.cartApiService.decreaseQuantity(cart)
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
                                    Toast.makeText(FoodDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                    quantity = quantity - 1.0;
                                    txtFoodDetailQuantity.setText(String.valueOf((int)quantity));
                                }
                            });
                } else if(quantity == 1){
                    CartApiService.cartApiService.deleteFoodFromCart(food.getFoodID(), user.getUserID())
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
                                    Toast.makeText(FoodDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                    quantity = quantity - 1.0;
                                    txtFoodDetailQuantity.setText(String.valueOf((int)quantity));
                                }
                            });
                }
            }
        });
    }

    private void reference(){
        imgFoodDetail = findViewById(R.id.imgFoodDetail);

        txtFoodNameDetail = findViewById(R.id.txtFoodNameDetail);
        txtFoodDescribeDetail = findViewById(R.id.txtFoodDescribeDetail);
        txtFoodPriceDetail = findViewById(R.id.txtFoodPriceDetail);
        txtFoodDetailQuantity = findViewById(R.id.txtFoodDetailQuantity);

        btnFoodDetailDecrease = findViewById(R.id.btnFoodDetailDecrease);
        btnFoodDetailIncrease = findViewById(R.id.btnFoodDetailIncrease);

    }
}