package com.vocketlist.android.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.VideoView;

import com.vocketlist.android.R;
import com.vocketlist.android.roboguice.log.Ln;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsit on 2017. 1. 30..
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.intro_video) protected VideoView mIntroVideoView;
    @BindView(R.id.skin) protected View mSkinView;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        mHandler = new Handler();
        playLogo();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void playLogo() {
        final String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.spalsh_logo;

        mIntroVideoView.setVideoURI(Uri.parse(uriPath));
        mIntroVideoView.requestFocus();
        mIntroVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Ln.e("play error");
                mIntroVideoView.setVisibility(View.GONE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startMainActivity();
                    }
                }, 1000);

                return false;
            }
        });

        mIntroVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               startMainActivity();
            }
        });
        mIntroVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSkinView.setVisibility(View.GONE);
                    }
                }, 700);

            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }
}
