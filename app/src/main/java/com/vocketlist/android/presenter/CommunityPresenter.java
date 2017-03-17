package com.vocketlist.android.presenter;

import com.vocketlist.android.dto.Post;
import com.vocketlist.android.api.vocket.Volunteer;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.presenter.basepresenter.BasePresenter;
import com.vocketlist.android.presenter.IView.ICommunityView;
import com.vocketlist.android.presenter.ipresenter.ICommunityPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kinamare on 2017-02-23.
 */

public class CommunityPresenter extends BasePresenter implements ICommunityPresenter {

	private ICommunityView iCommunityView;
	private ServiceDefine serviceManager;
	private List<Post> communityList;
	private List<Volunteer> volunteers;


	public CommunityPresenter(ICommunityView view) {
		iCommunityView = view;
		communityList = new ArrayList<>();
	}

	@Override
	public void getCommunityList() {
//		serviceManager.getCommunityList()
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new Subscriber<Response<Post>>() {
//					@Override
//					public void onCompleted() {
//
//					}
//
//					@Override
//					public void onError(Throwable e) {
//
//					}
//
//					@Override
//					public void onNext(Response<Post> postResponse) {
//
//					}
//				});


	}
}
