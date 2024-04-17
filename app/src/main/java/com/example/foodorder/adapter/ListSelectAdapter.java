package com.example.foodorder.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.models.ItemSelect;

import java.util.List;

public class ListSelectAdapter extends BaseAdapter {
    private Context context;
    private List<ItemSelect> itemSelects;

    public ListSelectAdapter(Context context, List<ItemSelect> itemSelects) {
        this.context = context;
        this.itemSelects = itemSelects;
    }

    @Override
    public int getCount() {
        return itemSelects.size();
    }

    @Override
    public Object getItem(int i) {
        return itemSelects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String title = itemSelects.get(i).getTitle();
        int image = itemSelects.get(i).getImage();
        TextView txtTitle;
        ImageView imageView;
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_select_layout, viewGroup, false);
            txtTitle = view.findViewById(R.id.txtSelect);
            imageView = view.findViewById(R.id.imgSelect);
            txtTitle.setText(title);
            imageView.setImageResource(image);
        } else{

        }
        return view;
    }
}
