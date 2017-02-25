package com.vocketlist.android.presenter.IView;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Schedule;

/**
 * Created by kinamare on 2017-02-26.
 */

public interface IScheduleView {
	void setScheduleList(BaseResponse<Schedule> scheduleList);
}
