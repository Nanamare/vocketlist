package com.vocketlist.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.vocketlist.android.R;
import com.vocketlist.android.activity.FacebookLoginActivity;
import com.vocketlist.android.api.user.MyListInfo;
import com.vocketlist.android.api.user.UserInfoModel;
import com.vocketlist.android.api.user.UserServiceManager;
import com.vocketlist.android.defined.RequestCode;
import com.vocketlist.android.preference.CommonPreference;
import com.vocketlist.android.preference.FacebookPreperence;
import com.vocketlist.android.roboguice.log.Ln;
import com.vocketlist.android.util.RxEventManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by SeungTaek.Lim on 2017. 2. 22..
 */

public class DrawerMenuFragment extends Fragment {

    private static final String TAG = DrawerMenuFragment.class.getSimpleName();

    public static final String MYLIST_TOTAL = "MYLIST_TOTAL";
    public static final String MYLIST_FINISH = "MYLIST_FINISH";

    @BindView(R.id.llLogin) protected LinearLayout mLoginView;
    @BindView(R.id.rlLogout) protected RelativeLayout mLogoutView;

    @BindView(R.id.civPhoto) CircleImageView civPhoto;
    @BindView(R.id.tvName) AppCompatTextView tvName;
    @BindView(R.id.tvProgress) AppCompatTextView tvProgress;
    @BindView(R.id.pbGoal) ProgressBar pbGoal;

//    protected CallbackManager mCallbackManager;

    private int totalCount;
    private int doneCount;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if(RequestCode.FACEBOOK_LOGIN == requestCode) {
            if(Activity.RESULT_OK == resultCode) initProfile();
        }
    }

    /**
     * 프로필설정
     */
    private void initProfile() {
        String profile = FacebookPreperence.getInstance().getUserImageUrl();
        String fullName = FacebookPreperence.getInstance().getUserName();

        // 사진
        if(!TextUtils.isEmpty(profile)) {
            Glide.with(this)
                    .load(profile)
                    .error(R.drawable.ci_inset)
                    .centerCrop()
                    .crossFade()
                    .into(civPhoto);
        }
        else civPhoto.setImageResource(R.drawable.ci_inset);

        // 이름
        tvName.setText(fullName);


        // 전체 갯수 / 완료 개수

        if(checkPreference()){
            totalCount = Integer.valueOf(CommonPreference.getInstance().getPreferenceString(MYLIST_TOTAL, null));
            doneCount = Integer.valueOf(CommonPreference.getInstance().getPreferenceString(MYLIST_FINISH, null));
        } else {
            totalCount = UserServiceManager.getLoginInfo().mUserInfo.mMyList.mTotal;
            doneCount = UserServiceManager.getLoginInfo().mUserInfo.mMyList.mFinish;
        }

        // 진행률
        pbGoal.setMax(totalCount);
        pbGoal.setProgress(doneCount);
        tvProgress.setText(getString(R.string.my_list_progress, totalCount, doneCount));

        RxEventManager.getInstance().getObjectObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Object>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Object o) {
                    if(o instanceof MyListInfo) refreshMyListCnt((MyListInfo)o);

                }
            });

    }

    private boolean checkPreference() {
        String total = CommonPreference.getInstance().getPreferenceString(MYLIST_TOTAL, null);
        String finish = CommonPreference.getInstance().getPreferenceString(MYLIST_FINISH, null);

        if(!TextUtils.isEmpty(total) && !TextUtils.isEmpty(finish)){
            return true;
        } else {
            return false;
        }
    }

    private void refreshMyListCnt(MyListInfo object) {

        totalCount = object.mTotal;
        doneCount = object.mFinish;

        //저장
        CommonPreference.getInstance().setPreference(MYLIST_TOTAL,String.valueOf(totalCount));
        CommonPreference.getInstance().setPreference(MYLIST_FINISH, String.valueOf(doneCount));

        pbGoal.setMax(totalCount);
        pbGoal.setProgress(doneCount);
        tvProgress.setText(getString(R.string.my_list_progress, totalCount, doneCount));
    }

    private void switchLogin() {
        mLoginView.setVisibility(View.VISIBLE);
        mLogoutView.setVisibility(View.GONE);

        initProfile();
    }

    private void switchLogout() {
        mLoginView.setVisibility(View.GONE);
        mLogoutView.setVisibility(View.VISIBLE);
        clearDrawerFragement();
    }

    private void clearDrawerFragement() {
        RxEventManager.getInstance().sendData(TAG);
    }

    private void refreshUserInfo() {
        if (UserServiceManager.isLogin()) {
            switchLogin();
        } else {
            switchLogout();
        }
    }
}