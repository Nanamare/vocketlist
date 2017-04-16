package com.vocketlist.android.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.vocketlist.android.R;

import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by kinamare on 2017-04-16.
 */

public class LocalSpinnerManager implements Spinner.OnItemSelectedListener {

	public static void getInstance(View localView, View localDetailView){

		final String [] city = {"서울특별시","인천광역시","부산광역시",
				"대전광역시","대구광역시","광주광역시","울산광역시"};
		final String [] gu = {"동구","서구","남구","북구","남동구"};

		/**
		 * 지역 설정
		 */
		final Spinner localSpinner = ButterKnife.findById(localView, R.id.popup_filter_local_spinner);

		ArrayAdapter localAdapter = new ArrayAdapter(
				getApplicationContext(), R.layout.custom_spinner_item, city);

		localAdapter.setDropDownViewResource(
				R.layout.custom_spinner_dropdown_item);

		localSpinner.setAdapter(localAdapter);

		/**
		 * 시군구 설정
		 */

		final Spinner localDetailSpinner = ButterKnife.findById(localDetailView, R.id.popup_filter_local_detail_spinner);

		ArrayAdapter localDetailAdapter = new ArrayAdapter(
				getApplicationContext(), R.layout.custom_spinner_item, gu);

		localDetailAdapter.setDropDownViewResource(
				R.layout.custom_spinner_dropdown_item);

		localDetailSpinner.setAdapter(localDetailAdapter);


	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view,
	                           int position, long id) {
//		txt_time.setText("position : " + position +
//				parent.getItemAtPosition(position));
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}


}
