package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.preference.NotificationPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * 알림설정
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class NotificationActivity extends DepthBaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.layout_noti_setting_line) protected View mLine;
    @BindView(R.id.layout_noti_setting_recommend) protected ViewGroup mLayoutRecommed;
    @BindView(R.id.layout_noti_setting_new_volunteer) protected ViewGroup mLayoutNewVolunteer;
    @BindView(R.id.layout_noti_setting_community) protected ViewGroup mLayoutCommunity;

    @BindView(R.id.switch_noti_setting_noti) protected SwitchCompat mNotiSetting;
    @BindView(R.id.switch_noti_setting_recommend) protected SwitchCompat mNotiRecommend;
    @BindView(R.id.switch_noti_setting_new_volunteer) protected SwitchCompat mNotiNewVolunteer;
    @BindView(R.id.switch_noti_setting_community) protected SwitchCompat mNotiCommunity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mNotiSetting.setChecked(NotificationPreference.getInstance().isNotiSetting());
        mNotiRecommend.setChecked(NotificationPreference.getInstance().isRecommend());
        mNotiNewVolunteer.setChecked(NotificationPreference.getInstance().isNewVolunteer());
        mNotiCommunity.setChecked(NotificationPreference.getInstance().isCommunity());
    }

    @OnCheckedChanged(R.id.switch_noti_setting_noti)
    protected void onCheckedNoti(boolean checked) {
        NotificationPreference.getInstance().setNotiSetting(checked);
    }

    @OnCheckedChanged(R.id.switch_noti_setting_recommend)
    protected void onCheckedRecommend(boolean checked) {
        NotificationPreference.getInstance().setRecommend(checked);
    }

    @OnCheckedChanged(R.id.switch_noti_setting_new_volunteer)
    protected void onCheckedNewVolunteer(boolean checked) {
        NotificationPreference.getInstance().setNewVolunteer(checked);
    }

    @OnCheckedChanged(R.id.switch_noti_setting_community)
    protected void oncheckedCommunity(boolean checked) {
        NotificationPreference.getInstance().setCommunity(checked);
    }
}
