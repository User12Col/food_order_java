package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodorder.R;

public class CartScreen extends AppCompatActivity {

    private Button buttonOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);

        buttonOrder = findViewById(R.id.button_order);
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
}
