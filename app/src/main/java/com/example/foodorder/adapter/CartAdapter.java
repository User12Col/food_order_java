package com.example.foodorder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.foodorder.api.CartApiService;
import com.example.foodorder.helper.Format;
import com.example.foodorder.models.Cart;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private TextView txtTotalPrice;
    private Context context;
    private List<Cart> carts;
    double totalPrice = 0;
//    double quantity = 0;

    public CartAdapter(Context context, List<Cart> carts) {
        this.context = context;
        this.carts = carts;
    }

    public CartAdapter(Context context, List<Cart> carts, TextView txtTotalPrice) {
        this.context = context;
        this.carts = carts;
        this.txtTotalPrice = txtTotalPrice;
    }

    private int calTotalPrice(List<Cart> carts){
        int total = 0;
        for (Cart cart : carts) {
            total = total + (int)cart.getTotalPrice();
        }
        return total;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.item_cart_layout, parent, false);
        ViewHolder viewHolder = new CartAdapter.ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        double[] price = {0};
        price[0] = carts.get(position).getQuantity() * carts.get(position).getFood().getUnitPrice();
        totalPrice = calTotalPrice(carts);
        txtTotalPrice.setText(Format.formatCurrency(totalPrice));

        holder.txtFoodCartName.setText(carts.get(position).getFood().getName());
        holder.txtQuantityCart.setText(String.valueOf(carts.get(position).getQuantity()));
        Picasso.get().load(carts.get(position).getFood().getImage()).into(holder.imgFoodCart);
        holder.txtFoodCartPrice.setText(Format.formatCurrency(price[0]));

        Food selectFood = carts.get(position).getFood();
        User user = DataLocalManager.getUser();

        double[] quantity = {carts.get(position).getQuantity()};

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
                        holder.txtQuantityCart.setText(String.valueOf((int) quantity[0]));
                    }
                });

        holder.btnIncreaseCart.setOnClickListener(new View.OnClickListener() {
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
                                quantity[0] = quantity[0] + 1;
                                price[0] = price[0] + selectFood.getUnitPrice();
                                totalPrice = totalPrice + selectFood.getUnitPrice();
                                holder.txtQuantityCart.setText(String.valueOf((int) quantity[0]));
                                holder.txtFoodCartPrice.setText(Format.formatCurrency(price[0]));
                                txtTotalPrice.setText(Format.formatCurrency(totalPrice));
                            }
                        });
            }
        });

        holder.btnDecreaseCart.setOnClickListener(new View.OnClickListener() {
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
                                    quantity[0] = quantity[0] - 1;
                                    price[0] = price[0] - selectFood.getUnitPrice();
                                    totalPrice = totalPrice - selectFood.getUnitPrice();
                                    holder.txtQuantityCart.setText(String.valueOf((int) quantity[0]));
                                    holder.txtFoodCartPrice.setText(Format.formatCurrency(price[0]));
                                    txtTotalPrice.setText(Format.formatCurrency(totalPrice));
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
//                                    quantity[0] = quantity[0] - 1;
//                                    holder.txtQuantityCart.setText(String.valueOf((int) quantity[0]));
                                    carts.remove(position);
                                    notifyDataSetChanged();
                                    txtTotalPrice.setText("");
                                }
                            });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtFoodCartName, txtFoodCartPrice, txtQuantityCart;
        private Button btnDecreaseCart, btnIncreaseCart;
        private ImageView imgFoodCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFoodCartName = itemView.findViewById(R.id.txtFoodCartName);
            txtFoodCartPrice = itemView.findViewById(R.id.txtCartPrice);
            txtQuantityCart = itemView.findViewById(R.id.txtQuantityCart);

            btnDecreaseCart = itemView.findViewById(R.id.btnDecreaseCart);
            btnIncreaseCart = itemView.findViewById(R.id.btnIncreaseCart);

            imgFoodCart = itemView.findViewById(R.id.imgFoodCart);

        }
    }
}
