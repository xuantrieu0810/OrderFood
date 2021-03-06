package com.lexuantrieu.orderfood.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.room.User;
import com.lexuantrieu.orderfood.model.room.database.AppDatabase;
import com.lexuantrieu.orderfood.presenter.MainActivityPresenter;
import com.lexuantrieu.orderfood.presenter.impl.MainActivityPresenterImpl;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View {

    TextView txtUsername, txtFullname, txtRole, txtToken;
    Button btnLogout, btnSet, btnKitChen, btnWaiter;
    MainActivityPresenter presenter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        presenter.invokeData();// kiem tra user trong database local

        btnSet.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SetFoodActivity.class);
            startActivity(intent);
        });
        btnWaiter.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ListTableActivity.class);
            startActivity(intent);
        });
        btnKitChen.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(view -> {
            presenter.onLogout();
        });

    }

    private void init() {
        db = AppDatabase.getInstance(this);
        presenter = new MainActivityPresenterImpl(this, this);
        btnKitChen = findViewById(R.id.btnKitchen);
        btnWaiter = findViewById(R.id.btnWaiter);
        btnSet = findViewById(R.id.btnSetFood);
        btnLogout = findViewById(R.id.btn_logout);
        txtUsername = findViewById(R.id.txt_username);
        txtFullname = findViewById(R.id.txt_fullname);
        txtRole = findViewById(R.id.txt_role);
        txtToken = findViewById(R.id.txt_token);
    }

    @Override
    public void onLogoutPending() {
        onStartProcessBar("Đang đăng xuất...");
    }

    @Override
    public void onLogoutSuccess() {
        onStopProcessBar();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Log.e("LXT_Log", "start LoginActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("LXT_Log", "finish MainActivity");
    }

    @Override
    public void onLogoutFail() {
        onStopProcessBar();
        Toast.makeText(this, "Lỗi đăng xuất", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInvokeDataSuccess(User user) {
        txtUsername.setText("Username: " + user.getUsername());
        txtFullname.setText("Tên: " + user.getFullname());
        txtToken.setText("Token: " + user.getToken());
        switch (user.getRole()) {
            case 1:
                txtRole.setText("Vai trò: Phục vụ");
                break;
            case 2:
                txtRole.setText("Vai trò: Đầu bếp");
                break;
            default:
                txtRole.setText("Vai trò: Quản lý");
                break;
        }
    }

    @Override
    public void onInvokeDataFail() {
        Toast.makeText(this, "Lỗi load User", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartProcessBar(String message) {

    }

    @Override
    public void onStopProcessBar() {

    }
}