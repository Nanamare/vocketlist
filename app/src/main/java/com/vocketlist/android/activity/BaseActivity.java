package com.vocketlist.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.vocketlist.android.R;

public class BaseActivity extends AppCompatActivity {
    private boolean mIsRootActivity = false;
    private boolean mCloseFlag = false;

    protected FirebaseAnalytics mFirebaseAnalytics;

    private final Handler mCloseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCloseFlag = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.color.transparent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public boolean onNavigateUp() {
        Intent upIntent = getSupportParentActivityIntent();

        if (upIntent == null) {
            finish();
            return false;
        }

        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            upIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(upIntent);

        } else {
            upIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(upIntent);
        }

        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mIsRootActivity) {
            if (mCloseFlag == false) {
                showToast(getResources().getString(R.string.finish_application), Toast.LENGTH_SHORT);

                mCloseFlag = true;
                mCloseHandler.sendEmptyMessageDelayed(0, 3000);
            } else {
                finish();
                onRootFinish();
            }
        } else {
            super.onBackPressed();
        }
    }

    protected void setRootActivity(boolean isRootActivity) {
        mIsRootActivity = isRootActivity;
    }

    protected void onRootFinish() {}

    public void showToast(final String message, final int duration) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, duration).show();
            }
        });
    }
}
