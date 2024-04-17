package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.api.UserApiService;
import com.example.foodorder.helper.DialogHelper;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;
import com.example.foodorder.validation.Validation;
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
    private TextView txtEmailError, txtPasswordError, txtCheckLogin;
    String message = "";
    User user;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        reference();

        user = new User();

        validate();

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
                DialogHelper dialogHelper = new DialogHelper(LoginScreen.this, dialog);
                dialogHelper.showLoadingDialog();
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
                                    }
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Toast.makeText(LoginScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {
                                if(message.equals("Login fail")){
                                    txtCheckLogin.setVisibility(View.VISIBLE);
                                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) txtCheckLogin.getLayoutParams();
                                    params.setMargins(0, 0, 0, 40);
                                    txtCheckLogin.setLayoutParams(params);
                                    txtCheckLogin.setText("Sai thông tin đăng nhập");
                                    dialogHelper.dismissLoadingDialog();
                                } else{
                                    DataLocalManager.setUser(user);
                                    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                                    startActivity(intent);
                                    dialogHelper.dismissLoadingDialog();
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

        txtEmailError = findViewById(R.id.txtCheckEmail);
        txtPasswordError = findViewById(R.id.txtCheckPassword);
        txtCheckLogin = findViewById(R.id.txtCheckLogin);
    }

    private void validate(){
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = charSequence.toString();
                if(!Validation.isValidEmail(email)){
                    txtEmailError.setVisibility(View.VISIBLE);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) txtEmailError.getLayoutParams();
                    params.setMargins(0, 0, 0, 30);
                    txtEmailError.setLayoutParams(params);
                    txtEmailError.setText("Email không hợp lệ");
                } else if(!Validation.isNotEmpty(email)){
                    txtEmailError.setVisibility(View.VISIBLE);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) txtEmailError.getLayoutParams();
                    params.setMargins(0, 0, 0, 30);
                    txtEmailError.setLayoutParams(params);
                    txtEmailError.setText("Vui lòng nhập email");
                } else{
                    txtEmailError.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                if(!Validation.isValidPasswordLenght(password)){
                    txtPasswordError.setVisibility(View.VISIBLE);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) txtPasswordError.getLayoutParams();
                    params.setMargins(0, 0, 0, 30);
                    txtPasswordError.setLayoutParams(params);
                    txtPasswordError.setText("Mật khẩu phải hơn 8 kí tự");
                } else if(!Validation.isNotEmpty(password)){
                    txtPasswordError.setVisibility(View.VISIBLE);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) txtPasswordError.getLayoutParams();
                    params.setMargins(0, 0, 0, 30);
                    txtPasswordError.setLayoutParams(params);
                    txtPasswordError.setText("Vui lòng nhập mật khẩu");
                } else{
                    txtPasswordError.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}