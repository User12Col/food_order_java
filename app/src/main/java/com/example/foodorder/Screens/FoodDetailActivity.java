package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodorder.R;
import com.example.foodorder.models.Food;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class FoodDetailActivity extends AppCompatActivity {

    ImageView imgFoodDetail;
    TextView txtFoodNameDetail, txtFoodDescribeDetail, txtFoodPriceDetail;

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
    }

    private void reference(){
        imgFoodDetail = findViewById(R.id.imgFoodDetail);

        txtFoodNameDetail = findViewById(R.id.txtFoodNameDetail);
        txtFoodDescribeDetail = findViewById(R.id.txtFoodDescribeDetail);
        txtFoodPriceDetail = findViewById(R.id.txtFoodPriceDetail);

    }
}