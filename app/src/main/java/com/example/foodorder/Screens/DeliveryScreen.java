package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityDeliveryScreenBinding;
import com.example.foodorder.fragment.fragment_order;
import com.example.foodorder.fragment.order.fragment_canceled;
import com.example.foodorder.fragment.order.fragment_delivered;
import com.example.foodorder.fragment.order.fragment_intransit;
import com.example.foodorder.fragment.order.fragment_preparing;

public class DeliveryScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_screen);
        displayDefaultFragment();
    }

    private void displayDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_delivery, new fragment_delivered());
        transaction.commit();
    }

    public void changeFragment(View view) {
        Fragment newFragment;
        int viewId = view.getId();
        if (viewId == R.id.completedOrder) {
            newFragment = new fragment_delivered();
        } else if (viewId == R.id.DeliveringOrder) {
            newFragment = new fragment_intransit();
        } else if (viewId == R.id.preparing) {
            newFragment = new fragment_preparing();
        } else if (viewId == R.id.canceled) {
            newFragment = new fragment_canceled();
        } else {
            newFragment = new fragment_delivered();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_delivery, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void goBackToPayScreen(View view) {
        // Tạo một Intent để quay về MainActivity
        Intent intent = new Intent(this, PayScreen.class);
        startActivity(intent); // Bắt đầu activity mới
    }

}