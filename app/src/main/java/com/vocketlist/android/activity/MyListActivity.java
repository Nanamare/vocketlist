package com.vocketlist.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.MyListAdapter;
import com.vocketlist.android.dto.MyList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 목표관리
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class MyListActivity extends DepthBaseActivity {
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.myList_lv)
	ListView myList_lv;

	private MyListAdapter adapter;
	private List<MyList> myLists;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mylist);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);


		MyList mockList = new MyList();
		myLists = new ArrayList<>();
		myLists.add(mockList);
		myLists.add(mockList);
		myLists.add(mockList);
		adapter = new MyListAdapter(this, myLists);
		myList_lv.setAdapter(adapter);
		View footer = getLayoutInflater().inflate(R.layout.mylistview_footer, null, false);
		myList_lv.addFooterView(footer);
		LinearLayout myList_add_goal_ll = (LinearLayout) footer.findViewById(R.id.myList_add_goal_ll);
		myList_add_goal_ll.setOnClickListener(view -> {
			MyList mockLists = new MyList();
			adapter.addMyList(mockLists);
			adapter.notifyDataSetChanged();
		});
	}


}
