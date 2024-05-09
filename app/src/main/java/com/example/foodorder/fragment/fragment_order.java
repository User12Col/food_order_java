package com.example.foodorder.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
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
import com.example.foodorder.adapter.CategoryAdapter;
import com.example.foodorder.adapter.FoodDiscountAdapter;
import com.example.foodorder.api.CategoryApiService;
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
    private List<Category> categories;
    private RecyclerView rclFoodByCategory, rclCategory;
    private LinearLayout saleLayout, pizzaLayout, friedChickenLayout, burgerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        reference(rootView);

        callApiGetCategory();
        callApi(new Category(1, "Chicken",0));

        return rootView;
    }

    private void callApi(Category category){
        if(!foods.isEmpty()){
            foods.clear();
        }
        FoodApiService.foodApiService.getFoodByCategory(category.getCateID())
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
                        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                return 1;
                            }
                        });
                        rclFoodByCategory.setLayoutManager(gridLayoutManager);
                        rclFoodByCategory.setAdapter(foodDiscountAdapter);

                    }
                });
    }

    private void callApiGetCategory(){
        CategoryApiService.categoryApiService.getAllCategory()
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
                            Type listType = new TypeToken<List<Category>>() {}.getType();
                            categories = gson.fromJson(gson.toJson(data), listType);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        LinearLayoutManager horizontalLayout = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity().getApplicationContext(), categories, new CategoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if(position!=0){
                                    int clickedPosition = rclCategory.getChildAdapterPosition(view);
                                    int middlePosition = rclCategory.getChildCount() / 2;
                                    int offset = clickedPosition - middlePosition;
                                    rclCategory.smoothScrollToPosition(clickedPosition + offset);
                                }
                                callApi(categories.get(position));
                            }
                        });
                        rclCategory.setLayoutManager(horizontalLayout);
                        rclCategory.setAdapter(categoryAdapter);
                    }
                });
    }

    private void reference(View view){
        rclFoodByCategory = view.findViewById(R.id.rclFoodByCategory);
        rclCategory = view.findViewById(R.id.rclCategory);

//        saleLayout = view.findViewById(R.id.sale);
//        pizzaLayout = view.findViewById(R.id.pizza);
//        friedChickenLayout = view.findViewById(R.id.fried_chicken);
//        burgerLayout = view.findViewById(R.id.burger);

        foods = new ArrayList<>();
    }
}
