package com.vocketlist.android.api.vocket;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.dto.VolunteerDetail;

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

public interface VocketService {

	@GET("services/list/")
	Observable<Response<BaseResponse<Volunteer>>> getVocketList(@Query("page") int page);

	@GET("services/list/")
	Observable<Response<BaseResponse<Volunteer>>> getVocketCategoryList(@Query("category") String category
	,@Query("page")int page);

	@GET("services/detail/{voketIdx}/")
	Observable<Response<BaseResponse<VolunteerDetail>>> getVocketDetail(@Path("voketIdx") int voketIdx);

	@FormUrlEncoded
	@POST("service/create")
	Observable<Response<ResponseBody>> addVocket(@Field("token") String token, @Field("name") String name
	, @Field("phone")int Phone, @Field("voketId")String voketId);

	@FormUrlEncoded
	@POST("services/participate/{service_id}/")
	Observable<Response<ResponseBody>> applyVolunteer(@Field("name")String name
	                                                  ,@Field("phone")String phone,@Path("service_id")int service_id);
}
