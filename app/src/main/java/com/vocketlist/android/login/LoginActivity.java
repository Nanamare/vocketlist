package com.vocketlist.android.login;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.vocketlist.android.BaseActivity;
import com.vocketlist.android.R;

import butterknife.ButterKnife;

/**
 * Created by SeungTaek.Lim on 2017. 2. 2..
 */

public class LoginActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
