package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.api.UserApiService;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginScreen extends AppCompatActivity {

    private Button btnLogin;
    private EditText edtEmail;
    private EditText edtPassword;
    private Button buttonToSignupScreen;
    String message = "";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        reference();

        user = new User();

        buttonToSignupScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình Signin
                Intent intent = new Intent(LoginScreen.this, SignupScreen.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                UserApiService.userApiService.login(email, password)
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
                                Toast.makeText(LoginScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {
                                if(message.equals("Login fail")){
                                    Toast.makeText(LoginScreen.this, "Sai thông tin đăng nhập", Toast.LENGTH_LONG).show();
                                } else{
                                    DataLocalManager.setUser(user);
                                    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });

    }

    private void reference(){
        buttonToSignupScreen = findViewById(R.id.button_to_signupscreen);
        btnLogin = findViewById(R.id.btnLogin);

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
    }
}