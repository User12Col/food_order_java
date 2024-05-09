package com.example.foodorder.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityMainBinding;
import com.example.foodorder.fragment.fragment_account;
import com.example.foodorder.fragment.fragment_home;
import com.example.foodorder.fragment.fragment_notification;
import com.example.foodorder.fragment.fragment_order;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ImageButton imageButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ánh xạ ImageButton
        imageButton3 = findViewById(R.id.image_button3);

        // Thiết lập trình nghe sự kiện cho ImageButton
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartScreen.class);
                startActivity(intent);
            }
        });

        replaceFragment(new fragment_home());
        binding.bottomNavigation.setBackground(null);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_tab_home) {
                replaceFragment(new fragment_home());
            } else if (item.getItemId() == R.id.menu_tab_order) {
                replaceFragment(new fragment_order());
            } else if (item.getItemId() == R.id.menu_tab_notification) {
                replaceFragment(new fragment_notification());
            } else if (item.getItemId() == R.id.menu_tab_account) {
                replaceFragment(new fragment_account());
            }
            return true;
        });

        String selectedTab = getIntent().getStringExtra("tab");
        if (selectedTab != null && selectedTab.equals("order")) {
            replaceFragment(new fragment_order());
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
