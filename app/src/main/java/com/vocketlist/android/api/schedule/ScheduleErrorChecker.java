package com.vocketlist.android.api.schedule;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Schedule;
import com.vocketlist.android.network.service.ErrorChecker;

/**
 * Created by kinamare on 2017-02-26.
 */

public class ScheduleErrorChecker implements ErrorChecker<BaseResponse<Schedule>> {
	@Override
	public void checkError(BaseResponse<Schedule> data) throws RuntimeException {
		if(data == null){
			throw new ScheduleError();
		}
	}
}
