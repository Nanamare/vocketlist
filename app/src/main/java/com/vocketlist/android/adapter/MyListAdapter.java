package com.vocketlist.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.MyListViewHolder;
import com.vocketlist.android.api.my.MyListModel;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;

import java.io.Serializable;
import java.util.List;

/**
 * 어댑터 : 도움말
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 22.
 */
public class MyListAdapter extends BaseAdapter<MyListModel.MyList, MyListViewHolder> {

	private RecyclerViewItemClickListener mListener;

	/**
	 * 생성자
	 *
	 * @param data
	 */
	public MyListAdapter(List<MyListModel.MyList> data, RecyclerViewItemClickListener listener) {
		super(data);
		mListener = listener;
	}

	@Override
	public MyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new MyListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_list, parent, false), mListener);
	}

	@Override
	public void onBindViewHolder(MyListViewHolder holder, int position) {
		holder.bind(getItem(position));
	}
}
