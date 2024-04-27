package com.example.foodorder.Screens;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.R;
import com.example.foodorder.api.UserApiService;
import com.example.foodorder.helper.DialogHelper;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.Role;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;
import com.example.foodorder.validation.Validation;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignupScreen extends AppCompatActivity {
    private Button buttonToLoginScreen;
    private Button btnSignUp;

    private EditText edtFullName;
    private EditText edtPhone;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtCfPassword;

    private TextView txtCheckName, txtCheckPhone, txtCheckEmail, txtCheckPassword, txtCheckCfPass, txtCheckSignUp;
    private Dialog dialog;

    User user;
    String message = "";
    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        reference();

        user = new User();

//        validate();

        buttonToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupScreen.this, LoginScreen.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check){
                    callApiSignUp();
                } else{

                }
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

        txtCheckName = findViewById(R.id.txtCheckName);
        txtCheckEmail = findViewById(R.id.txtCheckEmailSignUp);
        txtCheckPhone = findViewById(R.id.txtCheckPhone);
        txtCheckPassword = findViewById(R.id.txtCheckPass);
        txtCheckCfPass = findViewById(R.id.txtCheckCfPass);
        txtCheckSignUp = findViewById(R.id.txtCheckSignUp);
    }

    private void validate(){
        edtFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = charSequence.toString();
                if(!Validation.isValidName(name)){
                    txtCheckName.setVisibility(View.VISIBLE);
                    txtCheckName.setText("Tên không hợp lệ");
                } else{
                    txtCheckName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = charSequence.toString();
                if(!Validation.isValidEmail(email)){
                    txtCheckEmail.setVisibility(View.VISIBLE);
                    txtCheckEmail.setText("Email không hợp lệ");
                } else{
                    txtCheckEmail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = charSequence.toString();
                if(!Validation.isValidPhone(phone)){
                    txtCheckPhone.setVisibility(View.VISIBLE);
                    txtCheckPhone.setText("Số điện thoại không hợp lệ");
                } else{
                    txtCheckPhone.setVisibility(View.VISIBLE);
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
                    txtCheckPassword.setVisibility(View.VISIBLE);
                    txtCheckPassword.setText("Mật khẩu ít nhất 8 kí tự");
                } else{
                    txtCheckPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtCfPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = edtPassword.getText().toString();
                String cfPassword = charSequence.toString();
                if(!Validation.isMatchPassword(password, cfPassword)){
                    txtCheckPassword.setVisibility(View.VISIBLE);
                    txtCheckPassword.setText("Mật khẩu không trùng nhau");
                } else{
                    txtCheckPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void callApiSignUp(){
        DialogHelper dialogHelper = new DialogHelper(SignupScreen.this, dialog);
        dialogHelper.showLoadingDialog();
        String name = edtFullName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        User newUser = new User(name, "", phone, email, password, new Role(2, "customer"));
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
                            dialogHelper.dismissLoadingDialog();

                        } else{
                            txtCheckSignUp.setVisibility(View.VISIBLE);
                            txtCheckSignUp.setText("");
                        }
                    }
                });
    }
}
