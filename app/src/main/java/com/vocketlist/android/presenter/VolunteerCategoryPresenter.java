package com.vocketlist.android.presenter;

import com.google.gson.Gson;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.net.basepresenter.BasePresenter;
import com.vocketlist.android.presenter.IView.IVolunteerCategoryView;
import com.vocketlist.android.presenter.ipresenter.IVolunteerCategoryPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-02-22.
 */

public class VolunteerCategoryPresenter extends BasePresenter implements IVolunteerCategoryPresenter {

	private ServiceManager serviceManager;
	private IVolunteerCategoryView view;

	public VolunteerCategoryPresenter() {
		serviceManager = new ServiceManager();
	}

	public VolunteerCategoryPresenter(IVolunteerCategoryView view) {
		serviceManager = new ServiceManager();
		this.view = view;
	}

	@Override
	public void getVoketDetailList(String token) {
		serviceManager.getVoketDetailList(token)
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
							List<Volunteer> volunteerList = parseVoketList(json);


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


}
