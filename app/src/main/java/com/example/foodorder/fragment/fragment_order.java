package com.example.foodorder.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.adapter.FoodDiscountAdapter;
import com.example.foodorder.api.FoodApiService;
import com.example.foodorder.models.Category;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.ResponeObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class fragment_order extends Fragment {

    private List<Food> foods;
    private RecyclerView rclFoodByCategory;
    private LinearLayout saleLayout, pizzaLayout, friedChickenLayout, burgerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        reference(rootView);

        callApi(new Category(1, "Chicken"));

        saleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi(new Category(1, "Chicken"));
            }
        });

        pizzaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi(new Category(2, "Discount"));
            }
        });

        friedChickenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi(new Category(3, "Combo"));
            }
        });

        burgerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi(new Category(4, "Burger"));
            }
        });

        //changeFragment(new Menu_fragment_bestseller());
        return rootView;
    }

    private void callApi(Category category){
        if(!foods.isEmpty()){
            foods.clear();
        }
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
                            foods = gson.fromJson(gson.toJson(data), listType);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                        FoodDiscountAdapter foodDiscountAdapter = new FoodDiscountAdapter(getActivity().getApplicationContext(), foods);
                        rclFoodByCategory.setLayoutManager(gridLayoutManager);
                        rclFoodByCategory.setAdapter(foodDiscountAdapter);

                    }
                });
    }

    private void reference(View view){
        rclFoodByCategory = view.findViewById(R.id.rclFoodByCategory);

        saleLayout = view.findViewById(R.id.sale);
        pizzaLayout = view.findViewById(R.id.pizza);
        friedChickenLayout = view.findViewById(R.id.fried_chicken);
        burgerLayout = view.findViewById(R.id.burger);

        foods = new ArrayList<>();
    }
}
