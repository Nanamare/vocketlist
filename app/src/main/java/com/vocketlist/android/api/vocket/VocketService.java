package com.vocketlist.android.api.vocket;

import com.vocketlist.android.defined.Category;
import com.vocketlist.android.dto.BaseResponse;

import okhttp3.ResponseBody;
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
	Observable<Response<BaseResponse<Volunteer>>> getVocketCategoryList(@Query("category") Category category
																		, @Query("page")int page);

	@GET("services/detail/{vocketIdx}/")
	Observable<Response<BaseResponse<VolunteerDetail>>> getVocketDetail(@Path("vocketIdx") int vocketIdx);

	@FormUrlEncoded
	@POST("service/create")
	Observable<Response<ResponseBody>> addVocket(@Field("token") String token
												, @Field("name") String name
												, @Field("phone")int Phone
												, @Field("vocketId")String vocketId);

	@FormUrlEncoded
	@POST("services/participate/{service_id}/")
	Observable<Response<Void>> applyVolunteer(@Field("name")String name
	                                                  ,@Field("phone")String phone,@Path("service_id")int service_id);
}
