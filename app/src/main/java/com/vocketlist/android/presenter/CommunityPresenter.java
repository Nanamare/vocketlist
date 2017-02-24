package com.vocketlist.android.presenter;

import com.google.gson.Gson;
import com.vocketlist.android.dto.Post;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.net.basepresenter.BasePresenter;
import com.vocketlist.android.presenter.IView.ICommunityView;
import com.vocketlist.android.presenter.ipresenter.ICommunityPresenter;

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
 * Created by kinamare on 2017-02-23.
 */

public class CommunityPresenter extends BasePresenter implements ICommunityPresenter {

	private ICommunityView iCommunityView;
	private ServiceManager serviceManager;
	private List<Post> communityList;
	private List<Volunteer> volunteers;


	public CommunityPresenter(ICommunityView view) {
		iCommunityView = view;
		serviceManager = new ServiceManager();
		communityList = new ArrayList<>();
	}

	@Override
	public void getCommunityList() {
		serviceManager. getCommunityList()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<Post>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Response<Post> postResponse) {

					}
				});


	}
}
