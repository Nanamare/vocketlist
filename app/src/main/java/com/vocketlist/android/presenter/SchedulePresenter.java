package com.vocketlist.android.presenter;

import com.vocketlist.android.api.schedule.ScheduleServiceManager;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Schedule;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.presenter.IView.IScheduleView;
import com.vocketlist.android.presenter.ipresenter.ISchedulePresenter;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-02-26.
 */

public class SchedulePresenter implements ISchedulePresenter {

	private ServiceDefine serviceManager;
	private BaseResponse<Schedule> scheduleBaseResponse;
	private IScheduleView view;

	public SchedulePresenter(IScheduleView view){
		this.view = view;
	}

	@Override
	public void getScheduleList() {
		ScheduleServiceManager.getScheduleList()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Response<BaseResponse<Schedule>>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Response<BaseResponse<Schedule>> baseResponseResponse) {
						scheduleBaseResponse = baseResponseResponse.body();
						view.setScheduleList(scheduleBaseResponse);

					}
				});
	}
}
