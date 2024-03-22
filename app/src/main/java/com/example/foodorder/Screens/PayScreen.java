package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodorder.R;

public class PayScreen extends AppCompatActivity {

    private Button buttonPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_screen);

        buttonPay = findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayScreen.this, DeliveryScreen.class);
                startActivity(intent);
            }
        });
    }


    public void goBackToCartScreen(View view) {
        // Tạo một Intent để quay về MainActivity
        Intent intent = new Intent(this, CartScreen.class);
        startActivity(intent); // Bắt đầu activity mới
    }
}