package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.vocketlist.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 봉사활동 : 보기
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class VolunteerReadActivity extends DepthBaseActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_read);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }
}
