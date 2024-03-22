package com.example.foodorder.Screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.R;
import com.example.foodorder.api.UserApiService;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignupScreen extends AppCompatActivity {
    Button buttonToLoginScreen;
    Button btnSignUp;
    EditText edtFullName;
    EditText edtPhone;
    EditText edtEmail;
    EditText edtPassword;

    User user;
    String message = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        reference();

        user = new User();

        buttonToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình LoginScreen
                Intent intent = new Intent(SignupScreen.this, LoginScreen.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtFullName.getText().toString();
                String phone = edtPhone.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                User newUser = new User(name, "", phone, email, password);
                UserApiService.userApiService.signUp(newUser)
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
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Toast.makeText(SignupScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {
                                if(message.equals("Add user success")){
                                    DataLocalManager.setUser(user);
                                    Intent intent = new Intent(SignupScreen.this, MainActivity.class);
                                    startActivity(intent);

                                } else{
                                    Toast.makeText(SignupScreen.this, "Vui lòng thử lại sau", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


    }

    private void reference(){
        buttonToLoginScreen = findViewById(R.id.button_to_loginscreen);
        btnSignUp = findViewById(R.id.btnSignUp);

        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
    }
}
