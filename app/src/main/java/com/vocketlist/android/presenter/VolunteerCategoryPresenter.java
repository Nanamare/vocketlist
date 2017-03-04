package com.vocketlist.android.presenter;

import com.vocketlist.android.api.vocket.VocketServiceManager;
import com.vocketlist.android.defined.Category;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.vocket.Volunteer;
import com.vocketlist.android.api.vocket.VolunteerDetail;
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

	private IVolunteerCategoryView view;
	private BaseResponse<Volunteer> volunteer;
	private BaseResponse<VolunteerDetail> volunteerDetails;
	private IVolunteerReadView volunteerReadView;

	public VolunteerCategoryPresenter() {

	}

	public VolunteerCategoryPresenter(IVolunteerCategoryView view) {
		this.view = view;
	}

	public VolunteerCategoryPresenter(IVolunteerReadView view) {
		this.volunteerReadView = view;
	}

	@Override
	public void getVocketList(int page) {
		VocketServiceManager.getVocketList(page)
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
						view.getVocketList(volunteer);
					}
				});

	}

	@Override
	public void getVocketDetail(int vocketIdx) {
		VocketServiceManager.getVocketDetail(vocketIdx)
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
						volunteerReadView.bindVocketDetailData(volunteerDetails);
					}
				});
	}

	@Override
	public void getVocketCategoryList(Category category, int page) {
		VocketServiceManager.getVocketList(category, page)
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
						view.getVocketList(volunteer);
					}
				});

	}


}
