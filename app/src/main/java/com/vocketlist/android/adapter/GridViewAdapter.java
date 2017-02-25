package com.vocketlist.android.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vocketlist.android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kinamare on 2017-02-25.
 */

public class GridViewAdapter extends android.widget.BaseAdapter {

	private List<String> voketCategoryList;
	private Context context;

	private String[] string = {"일손지원","주거환경","의료관련","행정지원","안전예방","상담봉사","교육봉사"
			,"멘토링","문화체육","환경","국제/해외","공익인권","이미용","기타"};

	public GridViewAdapter(Context context) {
		voketCategoryList = new ArrayList<>();
		this.context = context;
		for(int i = 0; i< string.length; i++){
			voketCategoryList.add(string[i]);
		}
	}

	@Override
	public int getCount() {
		return voketCategoryList.size();
	}

	@Override
	public Object getItem(int i) {
		return voketCategoryList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View converView, ViewGroup viewGroup) {

		final ViewHolder holder;

		if (converView == null) {
			converView = View.inflate(context, R.layout.item_gridview, null);
			holder = new ViewHolder(converView);
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}
		holder.cgTv.setText(voketCategoryList.get(position));
		holder.radioButton.setChecked(true);

		return converView;
	}

	static class ViewHolder {

		@BindView(R.id.radioBtn)
		AppCompatRadioButton radioButton;
		@BindView(R.id.cgTv)
		TextView cgTv;


		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
