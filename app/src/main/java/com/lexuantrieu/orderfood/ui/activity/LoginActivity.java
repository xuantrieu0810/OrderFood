package com.lexuantrieu.orderfood.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.room.database.AppDatabase;
import com.lexuantrieu.orderfood.presenter.LoginPresenter;
import com.lexuantrieu.orderfood.presenter.impl.LoginPresenterImpl;

import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View {

    public Disposable disposable;
    private AppDatabase db;
    private ProgressDialog dialog;
    private LoginPresenter loginPresenter;
    private EditText edt_username;
    private EditText edt_password;
    private Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        loginPresenter.onCheckToken();// check token local
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.requestLogin(edt_username.getText().toString(),edt_password.getText().toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.onDisCompositeDisposable();
        Log.e("LXT_Log","finish LoginActivity");
    }

    @Override
    public void onLoginSuccess() {
        onStopProcessBar();
        Log.e("LXT_Log","start MainActivity");

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onLoginFail() {
        onStopProcessBar();
        Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginPending() {
        onStartProcessBar("Đang đăng nhập...");
    }

    @Override
    public void onStartProcessBar(String message) {
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onStopProcessBar() {
        dialog.dismiss();
    }

    private void init(){
        db = AppDatabase.getInstance(this);
        loginPresenter = new LoginPresenterImpl(this,this,db);
        dialog = new ProgressDialog(this);
        edt_username=findViewById(R.id.edt_username);
        edt_password=findViewById(R.id.edt_password);
        btn_login=findViewById(R.id.btn_login);
    }
}
