package com.example.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.helper.Format;
import com.example.foodorder.models.OrderDetail;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>{
    private Context context;
    private List<OrderDetail> orderDetails;

    public OrderDetailAdapter(Context context, List<OrderDetail> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.item_orderdetail_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtOrDeName.setText(orderDetails.get(position).getFoods().getName());
        holder.txtOrDePrice.setText(Format.formatCurrency(orderDetails.get(position).getFoods().getUnitPrice() * orderDetails.get(position).getQuantity()));
        holder.txtOrDeQuantity.setText("x"+String.valueOf(orderDetails.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtOrDeName, txtOrDeQuantity, txtOrDePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrDeName = itemView.findViewById(R.id.txtOrDeName);
            txtOrDeQuantity = itemView.findViewById(R.id.txtOrDeQuantity);
            txtOrDePrice = itemView.findViewById(R.id.txtOrDePrice);
        }
    }
}
