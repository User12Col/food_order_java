package com.example.foodorder.adapter;

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
    private Context context;
    private List<Cart> carts;
    double quantity = 0;
    double price = 0;

    public CartAdapter(Context context, List<Cart> carts) {
        this.context = context;
        this.carts = carts;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtFoodCartName.setText(carts.get(position).getFood().getName());
        holder.txtQuantityCart.setText(String.valueOf(carts.get(position).getQuantity()));
        Picasso.get().load(carts.get(position).getFood().getImage()).into(holder.imgFoodCart);
        holder.txtFoodCartPrice.setText(String.valueOf(carts.get(position).getQuantity() * (int)carts.get(position).getFood().getUnitPrice()));

        Food selectFood = carts.get(position).getFood();
        User user = DataLocalManager.getUser();

        price = carts.get(position).getQuantity() * carts.get(position).getFood().getUnitPrice();

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
                            quantity = (Double) data;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        holder.txtQuantityCart.setText(String.valueOf((int)quantity));
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
                                quantity = quantity + 1;
                                price = price + selectFood.getUnitPrice();
                                holder.txtQuantityCart.setText(String.valueOf((int)quantity));
                                holder.txtFoodCartPrice.setText(String.valueOf((int)price));
                            }
                        });
            }
        });

        holder.btnDecreaseCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity > 1){
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
                                    quantity = quantity - 1;
                                    price = price - selectFood.getUnitPrice();
                                    holder.txtQuantityCart.setText(String.valueOf((int)quantity));
                                    holder.txtFoodCartPrice.setText(String.valueOf((int)price));
                                }
                            });
                } else if(quantity == 1){
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
                                    quantity = quantity - 1;
                                    holder.txtQuantityCart.setText(String.valueOf((int)quantity));
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
