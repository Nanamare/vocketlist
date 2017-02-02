package com.sicamp.android.noti.login;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.firebase.auth.FirebaseAuth;
import com.sicamp.android.noti.BaseActivity;
import com.sicamp.android.noti.R;
import com.sicamp.android.roboguice.util.Ln;

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

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Ln.d("already signed in");
        } else {
            Ln.d("already signed in");
            // not signed in
        }
    }
}
