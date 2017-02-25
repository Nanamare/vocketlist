package com.vocketlist.android.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.vocketlist.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsit on 2017. 1. 30..
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.intro) protected ImageView mIntroView;

    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        startIntro();
    }

    protected void startIntro() {
        AnimationDrawable drawable = (AnimationDrawable) mIntroView.getBackground();
        drawable.start();

        drawable.setOneShot(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 2100);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
