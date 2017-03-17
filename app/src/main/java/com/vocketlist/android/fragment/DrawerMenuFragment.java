package com.vocketlist.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.activity.FacebookLoginActivity;
import com.vocketlist.android.api.user.UserServiceManager;
import com.vocketlist.android.defined.RequestCode;
import com.vocketlist.android.preference.FacebookPreperence;
import com.vocketlist.android.roboguice.log.Ln;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SeungTaek.Lim on 2017. 2. 22..
 */

public class DrawerMenuFragment extends Fragment {
    private static final String TAG = DrawerMenuFragment.class.getSimpleName();

    @BindView(R.id.llLogin) protected LinearLayout mLoginView;
    @BindView(R.id.rlLogout) protected RelativeLayout mLogoutView;

    @BindView(R.id.civPhoto) CircleImageView civPhoto;
    @BindView(R.id.tvName) AppCompatTextView tvName;
    @BindView(R.id.tvProgress) AppCompatTextView tvProgress;
    @BindView(R.id.pbGoal) ProgressBar pbGoal;

//    protected CallbackManager mCallbackManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.navigation_drawer_header, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshUserInfo();
    }

    private void refreshUserInfo() {
        if (UserServiceManager.isLogin()) {
            switchLogin();
        } else {
            switchLogout();
        }
    }

    @OnClick(R.id.login_button)
    protected void onClickFacebookLoginBtn() {
        Ln.d("facebook login button click");

        Intent intent = new Intent(getContext(), FacebookLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(intent, RequestCode.FACEBOOK_LOGIN);
    }

    @OnClick(R.id.btnLogout)
    protected void onClickLogoutBtn() {
        UserServiceManager.logout();
        refreshUserInfo();
    }

    /**
     * 프로필설정
     */
    private void initProfile() {
        String profile = FacebookPreperence.getInstance().getUserImageUrl();
        String fullName = FacebookPreperence.getInstance().getUserName();

        Log.d(TAG, "onActivityResult: profile:" + profile);
        Log.d(TAG, "onActivityResult: fullName:" + fullName);


        // 사진
        if(!TextUtils.isEmpty(profile)) {
            Glide.with(this)
                    .load(profile)
                    .centerCrop()
                    .crossFade()
                    .into(civPhoto);
        }

        // 이름
        tvName.setText(fullName);
    }

    private void switchLogin() {
        mLoginView.setVisibility(View.VISIBLE);
        mLogoutView.setVisibility(View.GONE);

        initProfile();
    }

    private void switchLogout() {
        mLoginView.setVisibility(View.GONE);
        mLogoutView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if(RequestCode.FACEBOOK_LOGIN == requestCode) {
            if(Activity.RESULT_OK == resultCode) initProfile();
        }
    }
}