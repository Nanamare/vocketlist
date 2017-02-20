package com.vocketlist.android.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vocketlist.android.R;


/**
 * 플래그먼트 : 기본
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class BaseFragment extends Fragment implements
        SearchView.OnQueryTextListener {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search, menu);
//        MenuTintUtil.tintAllIcons(menu, R.color.colorAccent);

        // 검색
        MenuItem mi = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) mi.getActionView();
//        sv.setIconified(false);
        sv.setQueryHint(getString(R.string.hint_search));
        sv.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
