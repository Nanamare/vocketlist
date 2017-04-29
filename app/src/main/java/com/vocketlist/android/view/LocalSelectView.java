package com.vocketlist.android.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.vocketlist.android.R;
import com.vocketlist.android.api.address.AddressFirstInfo;
import com.vocketlist.android.api.address.AddressServiceManager;
import com.vocketlist.android.dto.BaseResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by kinamare on 2017-04-29.
 * 지역 선택 뷰
 */

public class LocalSelectView extends LinearLayout implements Spinner.OnItemSelectedListener{

	private static final String TAG = LocalSelectView.class.getSimpleName();

	@BindView(R.id.local_spinner) AppCompatSpinner local_spinner;
	@BindView(R.id.local_detail_spinner) AppCompatSpinner local_detail_spinner;

	private Context context;
	private List<AddressFirstInfo> firstInfo;

	private String[] firstInfoArray;

	public LocalSelectView(Context context) {
		this(context, null);
	}

	public LocalSelectView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LocalSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		View v = LayoutInflater.from(context).inflate(R.layout.local_select_view, this, true);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT,
				1.0f);
		v.setLayoutParams(params);
		ButterKnife.bind(this, v);

		AddressServiceManager.getFirstAddress()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<BaseResponse<List<AddressFirstInfo>>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(Response<BaseResponse<List<AddressFirstInfo>>> baseResponseResponse) {
						firstInfo = baseResponseResponse.body().mResult;
						transferArray(firstInfo);
						initSpinner();
					}
				});
	}

	private void initSpinner() {

		ArrayAdapter localAdapter = new ArrayAdapter(
				getApplicationContext(), R.layout.custom_spinner_item, firstInfoArray);

		localAdapter.setDropDownViewResource(
				R.layout.custom_spinner_dropdown_item);

		local_spinner.setAdapter(localAdapter);
		local_spinner.setPrompt("도레미");

	}

	private void transferArray(List<AddressFirstInfo> firstInfo) {
		firstInfoArray = firstInfo.toArray(new String[firstInfo.size()]);
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		switch (view.getId()){
			case R.id.local_spinner : {
						Toast.makeText(context, "position : " + position +
				parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.local_detail_spinner : {

				break;
			}
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}


}
