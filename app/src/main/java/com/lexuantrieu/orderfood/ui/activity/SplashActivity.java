package com.lexuantrieu.orderfood.ui.activity;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.lexuantrieu.orderfood.R;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Show the splash screen

        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Start lengthy operation in a background thread

        new Thread(new Runnable() {

            public void run() {

                doLoadingWork();

                // add your code to redirect to main app screen

                finish();

            }

        }).start();

    }

    // Perform loading tasks in this method, also set the progress bar

    private void doLoadingWork() {

        for (int progress = 0; progress < 20; progress += 10) {
            try {
                Thread.sleep(700);
                progressBar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}