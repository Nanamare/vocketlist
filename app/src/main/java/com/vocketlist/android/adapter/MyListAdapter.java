package com.vocketlist.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.vocketlist.android.R;
import com.vocketlist.android.dto.MyList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kinamare on 2017-02-24.
 */

public class MyListAdapter extends android.widget.BaseAdapter {

	private List<MyList> myLists;
	private Context context;

	public MyListAdapter(Context context, List<MyList> lists){
		myLists = lists;
		this.context = context;

	}
	@Override
	public int getCount() {
		return myLists.size();
	}

	@Override
	public Object getItem(int i) {
		return myLists.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.mylistview_item, null);

			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	public static class ViewHolder {


		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}

	}

	public void addMyList(MyList myList){
		myLists.add(myList);

	}
}
