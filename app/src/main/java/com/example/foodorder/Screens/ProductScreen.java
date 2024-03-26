package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodorder.R;
import com.example.foodorder.adapter.ListAdapter;
import com.example.foodorder.data.ListDinksData;
import com.example.foodorder.databinding.ActivityProductScreenBinding;

import java.util.ArrayList;

public class ProductScreen extends AppCompatActivity {

    ActivityProductScreenBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListDinksData> dataArrayList = new ArrayList<>();
    ListDinksData listDinksData;

    Button btnDecrease, btnIncrease, btnAddToCart;
    TextView quantityText;
    int quantity = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imageList = {R.drawable.coca, R.drawable.pepsi,R.drawable.coca, R.drawable.pepsi, R.drawable.coca, R.drawable.pepsi,R.drawable.coca, R.drawable.pepsi};
//        int[] ingredientList = {R.string.COCAIngredients, R.string.PEPSIIngredients, R.string.COCALIngredients, R.string.PEPSILIngredients};
        String[] nameList = {"Coca M","Coca L", "Pepsi M", "Pepsi L", "Coca M","Coca L", "Pepsi M", "Pepsi L" };
        String[] priceList = {"15.000đ", "15.000đ", "20.000đ", "20.000đ", "15.000đ", "15.000đ", "20.000đ", "20.000đ"};

        for ( int i = 0; i< imageList.length; i++){
            listDinksData= new ListDinksData(nameList[i], priceList[i], imageList[i]);
            dataArrayList.add(listDinksData);
        }
        listAdapter = new ListAdapter(ProductScreen.this, dataArrayList);
        ListView listView = binding.drinkListview;
        listView.setAdapter(listAdapter);

        // Ánh xạ các thành phần từ layout
        btnDecrease = findViewById(R.id.btn_decrease);
        btnIncrease = findViewById(R.id.btn_increase);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        quantityText = findViewById(R.id.quantity_text);

        // Thiết lập sự kiện click cho các nút
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    quantityText.setText(String.valueOf(quantity));
                }
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi người dùng nhấn nút "Add to Cart"
                // Ví dụ: Thêm sản phẩm vào giỏ hàng
            }
        });
    }
}
