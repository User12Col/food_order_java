package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.api.UserApiService;
import com.example.foodorder.helper.DialogHelper;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.Role;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SetPasswordActivity extends AppCompatActivity {
    private TextView txtHeader, txtCheckUpdatePass;
    private Button btnComplete;
    private EditText edtGmailPass;
    private Dialog dialog;

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        reference();

        Intent intent = getIntent();
        Gson gson = new Gson();
        User user = gson.fromJson(intent.getStringExtra("gmailUser"), User.class);
        txtHeader.setText("Chào, "+ user.getName()+", nhập mật khẩu cho tài khoản của bạn!");
        user.setRole(new Role(2, "customer"));

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiSignUp(user);
            }
        });

    }

    private void reference(){
        txtHeader = findViewById(R.id.txtHeader);
        btnComplete = findViewById(R.id.btnComplete);
        edtGmailPass = findViewById(R.id.edtGmailPassword);
        txtCheckUpdatePass = findViewById(R.id.txtCheckUpdatePass);
    }

    private void callApiSignUp(User user){
        DialogHelper dialogHelper = new DialogHelper(SetPasswordActivity.this, dialog);
        dialogHelper.showLoadingDialog();
        String password = edtGmailPass.getText().toString();
        user.setPassword(password);
        UserApiService.userApiService.signUp(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponeObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponeObject responeObject) {
                        message = responeObject.getMessage();
                        Object data = responeObject.getData();

                        if(data instanceof LinkedTreeMap){
                            Gson gson = new Gson();
                            String json = gson.toJson(data);
                            User userData = gson.fromJson(json, User.class);
                            user.setUserID(userData.getUserID());
                            user.setName(userData.getName());
                            user.setAddress(userData.getAddress());
                            user.setEmail(userData.getEmail());
                            user.setPassword(userData.getPassword());
                            user.setPhone(userData.getPhone());
                            user.setRole(userData.getRole());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(SetPasswordActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        if(message.equals("Add user success")){
                            DataLocalManager.setUser(user);
                            Intent intent = new Intent(SetPasswordActivity.this, MainActivity.class);
                            startActivity(intent);
                            dialogHelper.dismissLoadingDialog();

                        } else if(message.equals("User has same email")){
                            txtCheckUpdatePass.setVisibility(View.VISIBLE);
                            txtCheckUpdatePass.setText("Tài khoản đã tồn tại");
                            dialogHelper.dismissLoadingDialog();

                        } else{
                            Toast.makeText(SetPasswordActivity.this, "Fail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}