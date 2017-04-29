package com.vocketlist.android.presenter;

import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Schedule;
import com.vocketlist.android.presenter.IView.IScheduleView;
import com.vocketlist.android.presenter.ipresenter.ISchedulePresenter;

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

	}
}
