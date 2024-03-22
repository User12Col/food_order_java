package com.example.foodorder.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.adapter.FoodDiscountAdapter;
import com.example.foodorder.api.FoodApiService;
import com.example.foodorder.models.Category;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.ResponeObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class fragment_home extends Fragment {

    private int currentValue = 0;
    private TextView textView;
    private RecyclerView rclDiscount;
    private List<Food> foodsDiscount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        reference(view);

        Category category = new Category(2,"Discount");

        FoodApiService.foodApiService.getFoodByCategory(category)
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
                            Type listType = new TypeToken<List<Food>>() {}.getType();
                            foodsDiscount = gson.fromJson(gson.toJson(data), listType);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        FoodDiscountAdapter foodDiscountAdapter = new FoodDiscountAdapter(getActivity().getApplicationContext(), foodsDiscount);
                        rclDiscount.setLayoutManager(horizontalLayoutManagaer);
                        rclDiscount.setAdapter(foodDiscountAdapter);
                    }
                });

        return view;
    }

    private void reference(View view){
        rclDiscount = view.findViewById(R.id.rclDiscount);
        textView = view.findViewById(R.id.count_product);
    }
}
