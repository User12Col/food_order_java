package com.example.foodorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.Screens.OrderDetailActivity;
import com.example.foodorder.models.Order;
import com.example.foodorder.models.OrderDetail;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders){
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.item_order_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.txtOrderID.setSingleLine(true);
        holder.txtOrderID.setEllipsize(TextUtils.TruncateAt.END);
        holder.txtAddress.setText(order.getAddress());
        holder.txtOrderDate.setText(order.getDate());
        holder.txtOrderStatus.setText(order.getStatus());
        holder.txtOrderID.setText(order.getOrderID());

        holder.itemOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), OrderDetailActivity.class);
                intent.putExtra("orderID", order.getOrderID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtAddress, txtOrderID, txtOrderDate, txtOrderStatus;
        private LinearLayout itemOrderLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtOrderID = itemView.findViewById(R.id.txtOrderID);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            itemOrderLayout = itemView.findViewById(R.id.itemOrderLayout);
        }
    }
}
