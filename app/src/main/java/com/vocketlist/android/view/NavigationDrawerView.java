package com.vocketlist.android.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.vocketlist.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SeungTaek.Lim on 2017. 2. 22..
 */

public class NavigationDrawerView extends FrameLayout {
    @BindView(R.id.view_holder_fragment) protected FrameLayout mFragmentHolder;

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    public NavigationDrawerView(Context context) {
        super(context);
    }

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.navigation_drawer_view, this, true);
        ButterKnife.bind(this);
    }

    public void setFragmentManager(FragmentManager manager, Fragment fragment) {
        mFragmentManager = manager;
        mFragment = fragment;
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == View.VISIBLE) {
            setFragment(mFragment);
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mFragmentManager.findFragmentByTag(fragment.getClass().getSimpleName()) != null) {
            return;
        }

        transaction.replace(R.id.view_holder_fragment, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }
}
