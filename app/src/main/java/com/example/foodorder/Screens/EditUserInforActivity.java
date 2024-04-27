package com.example.foodorder.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.api.UserApiService;
import com.example.foodorder.models.ResponeObject;
import com.example.foodorder.models.Role;
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocal;
import com.example.foodorder.storage.DataLocalManager;
import com.example.foodorder.validation.Validation;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditUserInforActivity extends AppCompatActivity {
    private TextView txtCheckNewName, txtCheckNewPhone, txtCheckUpdateUser;
    private EditText edtNewFullName, edtNewPhone;
    private Button btnUpdate;
    String messgage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_infor);

        reference();

        validate();

        User user = DataLocalManager.getUser();
        edtNewFullName.setText(user.getName());
        edtNewPhone.setText(user.getPhone());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Validation.isValidName(edtNewFullName.getText().toString())){
                    txtCheckUpdateUser.setVisibility(View.VISIBLE);
                    txtCheckUpdateUser.setText("Tên không hợp lệ");
                } else if(!Validation.isValidPhone(edtNewPhone.getText().toString())){
                    txtCheckUpdateUser.setVisibility(View.VISIBLE);
                    txtCheckUpdateUser.setText("Số điện thoạikhông hợp lệ");
                } else{
                    user.setName(edtNewFullName.getText().toString());
                    user.setPhone(edtNewPhone.getText().toString());
                    callApiUpdate(user.getUserID(), user);
                }
            }
        });
    }

    private void reference(){
        txtCheckNewName = findViewById(R.id.txtCheckNewName);
        txtCheckNewPhone = findViewById(R.id.txtCheckNewPhone);
        txtCheckUpdateUser = findViewById(R.id.txtCheckUpdateUser);

        edtNewFullName = findViewById(R.id.edtNewFullName);
        edtNewPhone = findViewById(R.id.edtNewPhone);

        btnUpdate = findViewById(R.id.btnUpdate);
    }

    private void validate(){
        edtNewFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = charSequence.toString();
                if(!Validation.isValidName(name)){
                    txtCheckNewName.setVisibility(View.VISIBLE);
                    txtCheckNewName.setText("Tên không hợp lệ");
                } else{
                    txtCheckNewName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtNewPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = charSequence.toString();
                if(!Validation.isValidPhone(phone)){
                    txtCheckNewPhone.setVisibility(View.VISIBLE);
                    txtCheckNewPhone.setText("Số điện thoại không hợp lệ");
                } else{
                    txtCheckNewPhone.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void callApiUpdate(String id, User user){
        UserApiService.userApiService.updateUser(id, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponeObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponeObject responeObject) {
                        messgage = responeObject.getMessage();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if(messgage.equals("Update success")){
                            DataLocalManager.setUser(user);
                            Toast.makeText(EditUserInforActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(EditUserInforActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}