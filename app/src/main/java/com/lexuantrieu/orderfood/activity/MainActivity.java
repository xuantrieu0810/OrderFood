package com.lexuantrieu.orderfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.lexuantrieu.orderfood.R;

public class MainActivity extends AppCompatActivity {

    Button btnGet, btnSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGet = findViewById(R.id.btnListFood);
        btnSet = findViewById(R.id.btnSetFood);

        Intent intent = new Intent(MainActivity.this,SplashActivity.class);
        startActivity(intent);

        btnSet.setOnClickListener(view -> {
            Intent intent1 = new Intent(MainActivity.this,SetFoodActivity.class);
            startActivity(intent1);
        });
        btnGet.setOnClickListener(view -> {
            Intent intent12 = new Intent(MainActivity.this,ListFoodCustom.class);
            startActivity(intent12);
        });
    }
}