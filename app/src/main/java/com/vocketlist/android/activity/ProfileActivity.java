package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.GridViewAdapter;

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
    GridView gridView;

    private GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        gridViewAdapter = new GridViewAdapter(getApplicationContext());
        gridView.setAdapter(gridViewAdapter);
        setSupportActionBar(toolbar);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }
}
