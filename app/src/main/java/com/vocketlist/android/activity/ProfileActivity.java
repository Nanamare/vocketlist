package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.GridViewAdapter;
import com.vocketlist.android.view.NonNestScrollGridView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 프로필관리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class ProfileActivity extends DepthBaseActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.profileGv)
    NonNestScrollGridView gridView;

    private GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        gridViewAdapter = new GridViewAdapter(getApplicationContext());
        gridView.setAdapter(gridViewAdapter);
        gridView.setNonScroll(true);
    }
}
