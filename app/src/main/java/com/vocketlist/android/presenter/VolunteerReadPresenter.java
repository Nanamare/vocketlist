package com.vocketlist.android.presenter;

import com.vocketlist.android.api.vocket.Participate;
import com.vocketlist.android.api.vocket.VocketServiceManager;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.presenter.IView.IVolunteerReadView;
import com.vocketlist.android.presenter.basepresenter.BasePresenter;
import com.vocketlist.android.presenter.ipresenter.IVolunteerReadPresenter;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-02-26.
 */

public class VolunteerReadPresenter extends BasePresenter implements IVolunteerReadPresenter {

	private IVolunteerReadView view;

	public VolunteerReadPresenter(IVolunteerReadView view) {
		this.view = view;
	}


	@Override
	public void applyVolunteer(int service_id, String name, String phone) {
		VocketServiceManager.applyVolunteer(service_id, name, phone)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<BaseResponse<Participate>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Response<BaseResponse<Participate>> baseResponseResponse) {
						onCompleted();
					}
				});
	}
}
