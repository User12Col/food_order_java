package com.example.foodorder.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.foodorder.R;
import com.example.foodorder.fragment.menu.Menu_fragment_bestseller;
import com.example.foodorder.fragment.menu.Menu_fragment_burger;
import com.example.foodorder.fragment.menu.Menu_fragment_friedchicken;
import com.example.foodorder.fragment.menu.Menu_fragment_pizza;
import com.example.foodorder.fragment.menu.Menu_fragment_sale;

public class fragment_order extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        // Lấy tham chiếu tới LinearLayout và thiết lập sự kiện click
        LinearLayout bestSellerLayout = rootView.findViewById(R.id.best_seller);
        LinearLayout saleLayout = rootView.findViewById(R.id.sale);
        LinearLayout pizzaLayout = rootView.findViewById(R.id.pizza);
        LinearLayout friedChickenLayout = rootView.findViewById(R.id.fried_chicken);
        LinearLayout burgerLayout = rootView.findViewById(R.id.burger);

        bestSellerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new Menu_fragment_bestseller());
            }
        });

        saleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new Menu_fragment_sale());
            }
        });

        pizzaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new Menu_fragment_pizza());
            }
        });

        friedChickenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new Menu_fragment_friedchicken());
            }
        });

        burgerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new Menu_fragment_burger());
            }
        });

        changeFragment(new Menu_fragment_bestseller());
        return rootView;
    }

    private void changeFragment(Fragment newFragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_order, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
