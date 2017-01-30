package com.sicamp.android.noti.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sicamp.android.noti.BaseActivity;
import com.sicamp.android.noti.main.MainActivity;
import com.sicamp.android.noti.R;

/**
 * Created by lsit on 2017. 1. 30..
 */

public class SplashActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 1000);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }
}
