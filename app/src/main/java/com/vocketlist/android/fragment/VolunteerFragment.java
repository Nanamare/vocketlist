package com.vocketlist.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vocketlist.android.R;
import com.vocketlist.android.activity.MainActivity;
import com.vocketlist.android.adapter.VolunteerAdapter;
import com.vocketlist.android.defined.Category;
import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.util.SharePrefUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 플래그먼트 : 봉사활동
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class VolunteerFragment extends BaseFragment {
	//
	@BindView(R.id.viewPager)
	ViewPager viewPager;

	//
	private VolunteerAdapter mAdapter;
	private ServiceManager manager;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_volunteer, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		MainActivity act = (MainActivity) getActivity();
		if (act == null) return;
		if (view == null) return;
		ButterKnife.bind(this, view);

		manager = new ServiceManager();

		// 뷰페이저
		mAdapter = new VolunteerAdapter(getChildFragmentManager());
		for (Category category : Category.values()) {
			mAdapter.addFragment(VolunteerCategoryFragment.newInstance(category), getString(category.getResId()));
		}
		viewPager.setAdapter(mAdapter);

		// 탭
		TabLayout tabLayout = (TabLayout) act.findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);

		manager.getVoketDetailList("token")
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<ResponseBody>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Response<ResponseBody> responseBodyResponse) {
						try {
							String json = responseBodyResponse.body().string();
							//


						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

	}
}
