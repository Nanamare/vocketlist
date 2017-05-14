package com.vocketlist.android.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.vocketlist.android.R;
import com.vocketlist.android.api.address.AddressModel;
import com.vocketlist.android.api.address.AddressServiceManager;
import com.vocketlist.android.api.address.FirstAddress;
import com.vocketlist.android.api.user.FavoritListModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by kinamare on 2017-04-29.
 * @author kinamare nanamare.tistory.com
 * 지역 선택 뷰
 */

public class LocalSelectView extends LinearLayout {

	private static final String TAG = LocalSelectView.class.getSimpleName();
	private static final int BASEVALUE = 0;

	@BindView(R.id.local_spinner) AppCompatSpinner local_spinner;
	@BindView(R.id.local_detail_spinner) AppCompatSpinner local_detail_spinner;

	private List<AddressModel.SecondAddress> getSecondAddress;
	private List<FirstAddress> sFirstAddress;
	private int localDetailId;

	private String formerLocalNm;

	private int firstPosition;
	private int secondPosition;

	public LocalSelectView(Context context) {
		this(context, null);
	}

	public LocalSelectView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LocalSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		View v = LayoutInflater.from(context).inflate(R.layout.local_select_view, this, true);
		ButterKnife.bind(this,v);

		initFirstSpinner();


	}


	private void initFirstSpinner() {

		sFirstAddress = AddressServiceManager.getFirstAddressList();
		String[] firstArray = new String[sFirstAddress.size()];

		for (int i =0; i < sFirstAddress.size(); i++){
			firstArray[i] = sFirstAddress.get(i).mName;
		}

		ArrayAdapter localAdapter = new ArrayAdapter(
				getApplicationContext(), R.layout.custom_spinner_item,firstArray);

		localAdapter.setDropDownViewResource(
				R.layout.custom_spinner_dropdown_item);

		local_spinner.setAdapter(localAdapter);
		local_spinner.setPrompt("지역 설정");

	}

	@OnItemSelected(R.id.local_spinner)
	void spinnerOnCick(int position){
		initSecondSpinner(position);
	}

	private void initSecondSpinner(int position) {

		getSecondAddress = AddressServiceManager.getSecondAddress(sFirstAddress.get(position).mName);
		String[] secondArray = new String[getSecondAddress.size()];

		for (int i =0; i < getSecondAddress.size(); i++){
			secondArray[i] = getSecondAddress.get(i).mAddressName;
		}

		ArrayAdapter localAdapter = new ArrayAdapter(
				getApplicationContext(), R.layout.custom_spinner_item, secondArray);

		localAdapter.setDropDownViewResource(
				R.layout.custom_spinner_dropdown_item);

		local_detail_spinner.setAdapter(localAdapter);
		local_detail_spinner.setPrompt("지역 세부 설정");
		if(sFirstAddress.get(position).mName.equals(formerLocalNm)) {
			local_detail_spinner.setSelection(secondPosition);
		} else {
			local_detail_spinner.setSelection(0);
		}

	}

	@OnItemSelected(R.id.local_detail_spinner)
	void detailSpinerOnclick(int position){
		localDetailId = getSecondAddress.get(position).mId;
	}

	public int getLocalDetailId(){
		return localDetailId;
	}

	public void setInitValue(FavoritListModel.Region mAddress) {

		List<Integer> array = new ArrayList<>();

		array.add(mAddress.mFirstAddressId);
		array.add(mAddress.mSecondAddressId);

		for (int position = 0; position < sFirstAddress.size(); position++) {
			if (array.get(0) == sFirstAddress.get(position).mId) {
				firstPosition = position;
			}
		}
		formerLocalNm = sFirstAddress.get(firstPosition).mName;
		getSecondAddress = AddressServiceManager.getSecondAddress(sFirstAddress.get(firstPosition).mName);

		for (int position = 0; position < getSecondAddress.size(); position++) {
			if (array.get(1) == getSecondAddress.get(position).mId) {
				secondPosition = position;
			}
		}


		local_spinner.setSelection(firstPosition);

	}

}
