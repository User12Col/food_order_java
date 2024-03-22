package com.example.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodorder.R;
import com.example.myapplication.data.ListDinksData;

import java.util.ArrayList;

//public class ListAdapter extends ArrayAdapter<ListDinksData> {
//    public ListAdapter(@NonNull Context context, ArrayList<ListDinksData> dataArrayList) {
//        super(context, R.layout.list_item, dataArrayList);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
//        ListDinksData listDinksData = getItem(position);
//
//        if (view == null){
//            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
//        }
//
//        ImageView listImage = view.findViewById(R.id.listImage);
//        TextView listName = view.findViewById(R.id.listName);
//        TextView listPrice = view.findViewById(R.id.listPrice);
//
//        listImage.setImageResource(listDinksData.image);
//        listName.setText(listDinksData.name);
//        listPrice.setText(listDinksData.price);
//
//        return view;
//    }
//}
public class ListAdapter extends ArrayAdapter<ListDinksData> {
    private int selectedPosition = -1;

    public ListAdapter(@NonNull Context context, ArrayList<ListDinksData> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ListDinksData listDinksData = getItem(position);

        ImageView listImage = listItemView.findViewById(R.id.listImage);
        TextView listName = listItemView.findViewById(R.id.listName);
        TextView listPrice = listItemView.findViewById(R.id.listPrice);
        RadioButton radioButton = listItemView.findViewById(R.id.chooseDrink);

        listImage.setImageResource(listDinksData.image);
        listName.setText(listDinksData.name);
        listPrice.setText(listDinksData.price);

        radioButton.setChecked(position == selectedPosition);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });

        return listItemView;
    }
}
