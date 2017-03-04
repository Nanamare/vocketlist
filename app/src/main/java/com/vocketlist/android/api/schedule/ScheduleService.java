package com.vocketlist.android.api.schedule;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Schedule;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-26.
 */

public interface ScheduleService {

	@GET("/api/schedule")
	Observable<Response<BaseResponse<Schedule>>> getScheduleList();

}
