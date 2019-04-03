package com.vocketlist.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vocketlist.android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kinamare on 2017-02-25.
 */

public class GridViewAdapter extends android.widget.BaseAdapter {

	private List<String> vocketCategoryList;
	private List<RadioButton> radioButtonList;
	private Context context;
	private int count;

	private String[] string = {"일손지원", "주거환경", "의료관련", "행정지원", "안전예방", "상담봉사", "교육봉사"
			, "멘토링", "문화체육", "환경", "국제/해외", "공익인권", "이미용", "기타"};

	public GridViewAdapter(Context context) {
		vocketCategoryList = new ArrayList<>();
		radioButtonList = new ArrayList<>();
		this.context = context;
		for (int i = 0; i < string.length; i++) {
			RadioButton button = new RadioButton(context);
			radioButtonList.add(button);
			vocketCategoryList.add(string[i]);
		}
		count = 0;
	}

	@Override
	public int getCount() {
		return vocketCategoryList.size();
	}

	@Override
	public Object getItem(int i) {
		return vocketCategoryList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		final ViewHolder holder;

		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_gridview, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.cgTv.setText(vocketCategoryList.get(position));
		for (int i = 0; i < radioButtonList.size(); i++) {
			if (radioButtonList.get(i).isChecked()) {
				count++;
			}
			if (count > 4) {
				for (int j = 0; j < radioButtonList.size(); j++) {
					radioButtonList.get(j).setChecked(false);
					Toast.makeText(context, "더 이상 추가할수 없습니다.", Toast.LENGTH_SHORT).show();
				}
			}
		}
		return convertView;
	}

	static class ViewHolder {

		@BindView(R.id.radioBtn)
		RadioButton radioButton;
		@BindView(R.id.cgTv)
		TextView cgTv;


		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
