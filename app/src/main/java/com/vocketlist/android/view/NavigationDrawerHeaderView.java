package com.vocketlist.android.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vocketlist.android.R;
import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.util.SharePrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 뷰 : 네이게이션 드로워 : 헤더
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class NavigationDrawerHeaderView extends RelativeLayout {

    @BindView(R.id.llLogin) LinearLayout llLogin;
    @BindView(R.id.llLogout) LinearLayout llLogout;

    @BindView(R.id.civPhoto) CircleImageView civPhoto;
    @BindView(R.id.tvName) AppCompatTextView tvName;
    @BindView(R.id.pbGoal) ProgressBar pbGoal;
    @BindView(R.id.tvProgress) AppCompatTextView tvProgress;

    @BindView(R.id.menuNoti) NavigationDrawerHeaderItemView menuNoti;
    @BindView(R.id.menuSchedule) NavigationDrawerHeaderItemView menuSchedule;
    @BindView(R.id.menuGoal) NavigationDrawerHeaderItemView menuGoal;

//    @BindView(R.id.facebook_login_button) protected LoginButton facebookLoginButton;

//    @OnClick(R.id.btnLogin)
//    void onLoginClick(AppCompatButton v) {
//        onElementsClickListener.onLoginClick(v);
//    }

    protected CallbackManager callbackManager;
    private ServiceManager serviceManager;

    @OnClick(R.id.btnLogout)
    void onLogoutClick(AppCompatButton v) {
        onElementsClickListener.onLogoutClick(v);
    }

    @OnClick(R.id.tvProgress)
    void onProgressClick(AppCompatTextView v) {
        onElementsClickListener.onMyPostClick(v);
    }

    @OnClick(R.id.menuNoti)
    void onNotiClick(NavigationDrawerHeaderItemView v) {
        onElementsClickListener.onNotificationClick(v);
    }

    @OnClick(R.id.menuSchedule)
    void onScheduleClick(NavigationDrawerHeaderItemView v) {
        onElementsClickListener.onScheduleClick(v);
    }

    @OnClick(R.id.menuGoal)
    void onGoalClick(NavigationDrawerHeaderItemView v) {
        onElementsClickListener.onGoalClick(v);
    }

    public boolean isLogined() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.isExpired() == false;
    }

    public interface OnElementsClickListener{
        void onLoginClick(View v);
        void onLogoutClick(View v);
        void onNotificationClick(View v);
        void onScheduleClick(View v);
        void onGoalClick(View v);
        void onMyPostClick(View v);
    }
    private OnElementsClickListener absOnElementsClickListener = new OnElementsClickListener() {
        @Override
        public void onLoginClick(View v) {
        }

        @Override
        public void onLogoutClick(View v) {
        }

        @Override
        public void onNotificationClick(View v) {
        }

        @Override
        public void onScheduleClick(View v) {
        }

        @Override
        public void onGoalClick(View v) {
        }

        @Override
        public void onMyPostClick(View v) {
        }
    };
    private OnElementsClickListener onElementsClickListener = absOnElementsClickListener;

    /**
     * 생성자
     * @param context
     */
    public NavigationDrawerHeaderView(Context context) {
        this(context, null);
    }

    /**
     * 생성자
     * @param context
     * @param attrs
     */
    public NavigationDrawerHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 생성자
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public NavigationDrawerHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        refreshUserStatus();
        initFacebook();
    }

    public void setOnElementsClickListener(OnElementsClickListener onElementsClickListener) {
        this.onElementsClickListener = onElementsClickListener;
    }

    public void refreshUserStatus() {
        if (isLogined()) {
            switchLoginStatus();
        } else {
            switchLogoutStatus();
        }
    }

    private void switchLoginStatus() {
        llLogin.setVisibility(View.VISIBLE);
        llLogout.setVisibility(View.GONE);
    }

    private void switchLogoutStatus() {
        llLogin.setVisibility(View.GONE);
        llLogout.setVisibility(View.VISIBLE);
    }

    public void registerCallback(CallbackManager callbackManager, FacebookCallback<LoginResult> facebookCallback) {
//        this.facebookLoginButton.registerCallback(callbackManager, facebookCallback);
    }

    private void initFacebook() {
        this.callbackManager = CallbackManager.Factory.create();

//        this.facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
//        this.facebookLoginButton.registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(final LoginResult loginResult) {
//                Ln.d("LoginManager.onSuccess");
//                AccessToken accessToken = loginResult.getAccessToken();
//                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String expiredAt = sdf.format(accessToken.getExpires().getTime());
//
//                setFacebookData(loginResult);
//            }
//
//            @Override
//            public void onCancel() {
//                Ln.d("LoginManager.onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Ln.d(exception, "LoginManager.onError");
//            }
//        });
    }

    public void setFacebookData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response", response.toString());

                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String gender = response.getJSONObject().getString("gender");


                            Profile profile = Profile.getCurrentProfile();
                            String id = profile.getId();
                            String link = profile.getLinkUri().toString();
                            Log.i("Link", link);
                            if (Profile.getCurrentProfile() != null) {
                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                            }

                            Log.i("Login" + "Email", email);
                            Log.i("Login" + "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);
                            Log.i("Login" + "Gender", gender);


                            String userInfo = object.toString();
                            String token = loginResult.getAccessToken().toString();
                            String userId = loginResult.getAccessToken().getUserId();
                            serviceManager.loginFb(userInfo, token, userId)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<Response<ResponseBody>>() {
                                        @Override
                                        public void onCompleted() {
                                            //완료 되었을시 페북 정보 클라이언트에 저장
                                            SharePrefUtil.putSharedPreference("email", email);
                                            SharePrefUtil.putSharedPreference("imgUrl", link);
                                            SharePrefUtil.putSharedPreference("fullName", lastName + firstName);

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(Response<ResponseBody> responseBodyResponse) {
                                            try {
                                                String json = responseBodyResponse.body().string();
                                                JsonObject obj = (JsonObject) new JsonParser().parse(json);
                                                JsonObject jsonObject = (JsonObject) obj.get("result");
                                                //페북 로그인시 서버에서 내려주는 100줄짜리 토큰
                                                String token = jsonObject.getAsJsonPrimitive("token").getAsString();
                                                SharePrefUtil.putSharedPreference("token", token);

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,verified,name,locale,gender,updated_time,link,id,timezone,email");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
