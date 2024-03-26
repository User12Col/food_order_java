package com.example.foodorder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.foodorder.R;
import com.example.foodorder.Screens.AddressActivity;
import com.example.foodorder.Screens.LoginScreen;
import com.example.foodorder.Screens.SignupScreen;

public class fragment_account extends Fragment {
    private Button loginButton;
    private Button signupButton;
    private LinearLayout itemAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        loginButton = view.findViewById(R.id.button_login);
        signupButton = view.findViewById(R.id.button_signup);
        itemAddress = view.findViewById(R.id.itemAddress);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginScreen.class);
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignupScreen.class);
                startActivity(intent);
            }
        });

        itemAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddressActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
