package com.example.foodorder.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.foodorder.R;
import com.example.foodorder.Screens.AddressActivity;
import com.example.foodorder.Screens.DeliveryScreen;
import com.example.foodorder.Screens.EditPasswordActivity;
import com.example.foodorder.Screens.EditUserInforActivity;
import com.example.foodorder.Screens.LoginScreen;
import com.example.foodorder.Screens.SignupScreen;
import com.example.foodorder.helper.DialogHelper;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;

public class fragment_account extends Fragment {
    private LinearLayout itemAddress, itemLogOut, itemOrder, itemEditPass;
    private TextView txtUserName, txtUserEmail, txtUserAddress, txtUserPhone, txtEditUser;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        reference(view);

        User user = DataLocalManager.getUser();
        checkEmpty(txtUserEmail, user.getEmail());
        checkEmpty(txtUserPhone, user.getPhone());
        checkEmpty(txtUserAddress, user.getAddress());
        checkEmpty(txtUserName, user.getName());

        txtEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditUserInforActivity.class);
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

        itemLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataLocalManager.deleteUser("user");
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginScreen.class);
                startActivity(intent);
            }
        });

        itemOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliveryScreen.class);
                startActivity(intent);
            }
        });

        itemEditPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditPasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void reference(View view){
        itemAddress = view.findViewById(R.id.itemAddress);
        itemLogOut = view.findViewById(R.id.itemLogOut);
        itemOrder = view.findViewById(R.id.itemOrder);
        itemEditPass = view.findViewById(R.id.itemEditPass);

        txtUserAddress = view.findViewById(R.id.txtUserAddress);
        txtUserName = view.findViewById(R.id.txtUserName);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        txtUserPhone = view.findViewById(R.id.txtUserPhone);
        txtEditUser = view.findViewById(R.id.txtEditUser);
    }

    private void checkEmpty(TextView textView, String content){
        if(content.isEmpty()){
            textView.setText("Chưa cập nhật");
        } else{
            textView.setText(content);
        }
    }
}
