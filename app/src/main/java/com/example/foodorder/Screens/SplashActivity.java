package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.api.UserApiService;
import com.example.foodorder.helper.DialogHelper;
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

public class SplashActivity extends AppCompatActivity {
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(DataLocalManager.getUser()!=null){
                    DialogHelper dialogHelper = new DialogHelper(SplashActivity.this, dialog);
                    dialogHelper.showLoadingDialog();

                    User user = DataLocalManager.getUser();
                    String email = user.getEmail();
                    String password = user.getPassword();
                    UserApiService.userApiService.login(email, password)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<ResponeObject>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onNext(@NonNull ResponeObject responeObject) {
                                    Object data = responeObject.getData();
                                    if(data!=null){
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
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Toast.makeText(SplashActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onComplete() {
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    dialogHelper.dismissLoadingDialog();
                                    finish();
                                }
                            });
                } else{
                    DialogHelper dialogHelper = new DialogHelper(SplashActivity.this, dialog);
                    dialogHelper.showLoadingDialog();
                    Intent intent = new Intent(SplashActivity.this, LoginScreen.class);
                    startActivity(intent);
                    finish();
                }

            }
        },3000);

    }
}