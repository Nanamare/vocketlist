package com.vocketlist.android.view;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.vocketlist.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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

    @OnClick(R.id.btnLogin)
    void onLoginClick(AppCompatButton v) {
        onElementsClickListener.onLoginClick(v);
    }
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
    }

    public void setOnElementsClickListener(OnElementsClickListener onElementsClickListener) {
        this.onElementsClickListener = onElementsClickListener;
    }
}
