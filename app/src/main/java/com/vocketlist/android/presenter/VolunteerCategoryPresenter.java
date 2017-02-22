package com.vocketlist.android.presenter;

import com.google.gson.Gson;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.dto.VolunteerDetail;
import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.net.basepresenter.BasePresenter;
import com.vocketlist.android.presenter.IView.IVolunteerCategoryView;
import com.vocketlist.android.presenter.IView.IVolunteerReadView;
import com.vocketlist.android.presenter.ipresenter.IVolunteerCategoryPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-02-22.
 */

public class VolunteerCategoryPresenter extends BasePresenter implements IVolunteerCategoryPresenter {

	private ServiceManager serviceManager;
	private IVolunteerCategoryView view;
	private List<Volunteer> volunteerList;
	private VolunteerDetail volunteerDetails;
	private IVolunteerReadView volunteerReadView;

	public VolunteerCategoryPresenter() {
		serviceManager = new ServiceManager();
	}

	public VolunteerCategoryPresenter(IVolunteerCategoryView view) {
		serviceManager = new ServiceManager();
		this.view = view;
		volunteerList = new ArrayList<>();
	}

	public VolunteerCategoryPresenter(IVolunteerReadView view) {
		serviceManager = new ServiceManager();
		this.volunteerReadView = view;
		volunteerList = new ArrayList<>();
	}

	@Override
	public void getVoketList(String token) {
		serviceManager.getVoketList(token)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<ResponseBody>>() {
					@Override
					public void onCompleted() {
						view.getVoketList(volunteerList);
					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Response<ResponseBody> responseBodyResponse) {
						try {
							String json = responseBodyResponse.body().string();
							volunteerList = parseVoketList(json);


						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					private List<Volunteer> parseVoketList(String json) {
						List<Volunteer> volunteerList = new ArrayList<>();
						try {
							JSONObject object = new JSONObject(json);
							JSONArray jsonArray = new JSONArray(object.getString("result"));
							String VoketJson = jsonArray.toString();
							volunteerList.addAll(new Gson().fromJson(VoketJson, Volunteer.getListType()));

						} catch (JSONException e) {
							e.printStackTrace();
						}
						return volunteerList;
					}

				});
	}

	@Override
	public void getVoketDetail(String token) {
		serviceManager.getVoketDetail(token)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<ResponseBody>>() {
					@Override
					public void onCompleted() {
						volunteerReadView.bindVoketDetailData(volunteerDetails);
					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Response<ResponseBody> responseBodyResponse) {
						try {
							String json = responseBodyResponse.body().string();
							volunteerDetails = parseVoketDetail(json);

						} catch (IOException e) {
							e.printStackTrace();
						}

					}


					private VolunteerDetail parseVoketDetail(String json) {
						VolunteerDetail voketDetail = new VolunteerDetail();
						Gson gson = new Gson();
						try {
							JSONObject object = new JSONObject(json);
							JSONArray jsonArray = new JSONArray(object.getString("result"));
							String voketDetailJson = jsonArray.toString();
							voketDetail = gson.fromJson(voketDetailJson, VolunteerDetail.class);

						} catch (JSONException e) {
							e.printStackTrace();
						}
						return voketDetail;
					}

				});
	}


}
