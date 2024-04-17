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
import com.example.foodorder.models.User;
import com.example.foodorder.storage.DataLocalManager;
import com.example.foodorder.validation.Validation;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditPasswordActivity extends AppCompatActivity {
    private EditText edtOldPassword, edtNewPassword, edtConfirmNewPass;
    private TextView txtCheckOldPass, txtCheckNewPass, txtCheckCfNewPass, txtCheckUpdatePass;
    private Button btnUpdatePass;
    String messgage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        reference();

        User user = DataLocalManager.getUser();
        validate();
        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currPass = user.getPassword();
                if(!currPass.equals(edtOldPassword.getText().toString())){
                    txtCheckUpdatePass.setVisibility(View.VISIBLE);
                    txtCheckUpdatePass.setText("Mật khẩu hiện tại không trùng khớp");
                } else if(!Validation.isMatchPassword(edtNewPassword.getText().toString(),edtConfirmNewPass.getText().toString())){
                    txtCheckUpdatePass.setVisibility(View.VISIBLE);
                    txtCheckUpdatePass.setText("Mật khẩu mới không trùng nhau");
                } else if(!Validation.isValidPasswordLenght(edtNewPassword.getText().toString())){
                    txtCheckUpdatePass.setVisibility(View.VISIBLE);
                    txtCheckUpdatePass.setText("Mật khẩu mới không hợp lệ");
                } else{
                    user.setPassword(edtConfirmNewPass.getText().toString());
                    DataLocalManager.setUser(user);
                    callApiUpdatePassword(user.getUserID(), user);
                }
            }
        });

    }

    private void reference(){
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmNewPass = findViewById(R.id.edtConfirmNewPass);

        txtCheckOldPass = findViewById(R.id.txtCheckOldPass);
        txtCheckNewPass = findViewById(R.id.txtCheckNewPass);
        txtCheckCfNewPass = findViewById(R.id.txtCheckCfNewPass);
        txtCheckUpdatePass = findViewById(R.id.txtCheckUpdatePass);

        btnUpdatePass = findViewById(R.id.btnUpdatePass);
    }

    private void validate(){

        edtOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                if(!Validation.isValidPasswordLenght(password)){
                    txtCheckOldPass.setVisibility(View.VISIBLE);
                    txtCheckOldPass.setText("Mật khẩu ít nhất 8 kí tự");
                } else{
                    txtCheckOldPass.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtConfirmNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = edtNewPassword.getText().toString();
                String cfPassword = charSequence.toString();
                if(!Validation.isMatchPassword(password, cfPassword)){
                    txtCheckNewPass.setVisibility(View.VISIBLE);
                    txtCheckNewPass.setText("Mật khẩu không trùng nhau");
                } else{
                    txtCheckNewPass.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void callApiUpdatePassword(String id, User user){
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
                            Toast.makeText(EditPasswordActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(EditPasswordActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}