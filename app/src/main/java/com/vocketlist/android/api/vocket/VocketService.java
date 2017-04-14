package com.vocketlist.android.api.vocket;

import com.vocketlist.android.defined.Category;
import com.vocketlist.android.dto.BaseResponse;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-21.
 */

interface VocketService {

	@GET("services/list/")
	Observable<Response<BaseResponse<Volunteer>>> getVocketCategoryList(@Query("search") String category,
																		@Query("page")int page,
																		@Query( value = "search", encoded = true) String search);

	@GET("services/detail/{vocketIdx}/")
	Observable<Response<BaseResponse<VolunteerDetail>>> getVocketDetail(@Path("vocketIdx") int vocketIdx);

	@FormUrlEncoded
	@POST("services/participate/{service_id}/")
	Observable<Response<BaseResponse<Participate>>> participate(@Path("service_id")int service_id,
																@Field("name")String name,
																@Field("phone")String phone);
}
