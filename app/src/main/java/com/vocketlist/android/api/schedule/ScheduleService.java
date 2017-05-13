package com.vocketlist.android.api.schedule;

import com.vocketlist.android.dto.BaseResponse;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-26.
 */

public interface ScheduleService {
	@GET("schedules/")
	Observable<Response<BaseResponse<ScheduleListModel>>> list();

	@FormUrlEncoded
	@POST("schedules/")
	Observable<Response<BaseResponse<ScheduleWriteModel>>> write(@Field("title") String title,
																 @Field("start_date") String startDate,
																 @Field("end_date") String endDate,
																 @Field("start_time") String startTime,
																 @Field("end_time") String endTime,
																 @Field("place") String place,
																 @Field("is_done") boolean isDone);

	@DELETE("schedules/{id}/")
	Observable<Response<BaseResponse<ScheduleDeleteModel>>> delete(@Path("id") int id);

	@GET("schedules/{id}/")
	Observable<Response<BaseResponse<ScheduleModel>>> detail(@Path("id") int id);

	@FormUrlEncoded
	@PATCH("schedules/{id}/")
	Observable<Response<BaseResponse<ScheduleWriteModel>>> modify(@Field("title") String title,
																  @Field("start_date") String startDate,
																  @Field("end_date") String endDate,
																  @Field("start_time") String startTime,
																  @Field("end_time") String endTime,
																  @Field("place") String place,
																  @Field("is_done") boolean isDone);
}
