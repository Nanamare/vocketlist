package com.vocketlist.android.presenter;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.dto.VolunteerDetail;
import com.vocketlist.android.api.ServiceManager;
import com.vocketlist.android.api.basepresenter.BasePresenter;
import com.vocketlist.android.presenter.IView.IVolunteerCategoryView;
import com.vocketlist.android.presenter.IView.IVolunteerReadView;
import com.vocketlist.android.presenter.ipresenter.IVolunteerCategoryPresenter;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-02-22.
 */

public class VolunteerCategoryPresenter extends BasePresenter implements IVolunteerCategoryPresenter {

	private ServiceManager serviceManager;
	private IVolunteerCategoryView view;
	private BaseResponse<Volunteer> volunteer;
	private BaseResponse<VolunteerDetail> volunteerDetails;
	private IVolunteerReadView volunteerReadView;

	public VolunteerCategoryPresenter() {
		serviceManager = new ServiceManager();
	}

	public VolunteerCategoryPresenter(IVolunteerCategoryView view) {
		serviceManager = new ServiceManager();
		this.view = view;
	}

	public VolunteerCategoryPresenter(IVolunteerReadView view) {
		serviceManager = new ServiceManager();
		this.volunteerReadView = view;
	}

	@Override
	public void getVoketList(int page) {
		serviceManager.getVoketList(page)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<BaseResponse<Volunteer>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();

					}

					@Override
					public void onNext(Response<BaseResponse<Volunteer>> baseResposeResponse) {
						volunteer = baseResposeResponse.body();
						view.getVoketList(volunteer);
					}
				});

	}

	@Override
	public void getVoketDetail(int voketId) {
		serviceManager.getVoketDetail(voketId)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<BaseResponse<VolunteerDetail>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(Response<BaseResponse<VolunteerDetail>> baseResposeResponse) {
						volunteerDetails = baseResposeResponse.body();
						volunteerReadView.bindVoketDetailData(volunteerDetails);
					}
				});
	}

	@Override
	public void getVocketCategoryList(String category,int page) {
		serviceManager.getVocketCategoryList(category, page)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<BaseResponse<Volunteer>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Response<BaseResponse<Volunteer>> baseResponseResponse) {
						volunteer = baseResponseResponse.body();
						view.getVoketList(volunteer);
					}
				});

	}


}
