package com.example.foodorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.Screens.FoodDetailActivity;
import com.example.foodorder.api.CartApiService;
import com.example.foodorder.helper.Format;
import com.example.foodorder.models.Cart;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FoodHomeAdapter extends RecyclerView.Adapter<FoodHomeAdapter.ViewHolder>{
    private Context context;
    private List<Food> foods;
//    double quantity = 0;

    public FoodHomeAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.food_home_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtFoodName.setText(foods.get(position).getName());
        holder.txtFoodDescribe.setText(foods.get(position).getDescription());
        holder.txtFoodPrice.setText(Format.formatCurrency(foods.get(position).getUnitPrice()));

        Picasso.get().load(foods.get(position).getImage()).into(holder.imgFood);

        double[] quantity = {0};

        Food selectFood = foods.get(position);
        User user = DataLocalManager.getUser();

        CartApiService.cartApiService.getQuantity(selectFood.getFoodID(), user.getUserID())
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
                            quantity[0] = (Double) data;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        holder.txtQuantity.setText(String.valueOf((int) quantity[0]));
                    }
                });

        holder.foodItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Intent intent = new Intent(context.getApplicationContext(), FoodDetailActivity.class);
                intent.putExtra("food", gson.toJson(selectFood));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart cart = new Cart(selectFood, user, 1, selectFood.getUnitPrice());
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
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                                quantity[0] = quantity[0] + 1.0;
                                holder.txtQuantity.setText(String.valueOf((int) quantity[0]));
                            }
                        });
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity[0] > 1){
                    Cart cart = new Cart(selectFood, user, 1, selectFood.getUnitPrice());
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
                                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                    quantity[0] = quantity[0] - 1.0;
                                    holder.txtQuantity.setText(String.valueOf((int) quantity[0]));
                                }
                            });
                } else if(quantity[0] == 1){
                    CartApiService.cartApiService.deleteFoodFromCart(selectFood.getFoodID(), user.getUserID())
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
                                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                    quantity[0] = quantity[0] - 1.0;
                                    holder.txtQuantity.setText(String.valueOf((int) quantity[0]));
                                }
                            });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtFoodName, txtFoodDescribe, txtFoodPrice, txtQuantity;
        private Button btnDecrease, btnIncrease;
        private ImageView imgFood;
        private LinearLayout foodItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtFoodDescribe = itemView.findViewById(R.id.txtFoodDescribe);
            txtFoodPrice = itemView.findViewById(R.id.txtFoodPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);

            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);

            imgFood = itemView.findViewById(R.id.imgFood);

            foodItemLayout = itemView.findViewById(R.id.foodItemLayout);

        }
    }
}
