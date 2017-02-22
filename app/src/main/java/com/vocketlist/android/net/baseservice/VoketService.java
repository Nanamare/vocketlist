package com.vocketlist.android.net.baseservice;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-21.
 */

public interface VoketService {

	@GET("services/list")
	Observable<Response<ResponseBody>> getVoketList(@Query("token") String token);

	@GET("service/detail")
	Observable<Response<ResponseBody>> getVoketDetail(@Query("token") String token);

	@FormUrlEncoded
	@POST("service/create")
	Observable<Response<ResponseBody>> addVoket(@Field("token") String token, @Field("name") String name
	,@Field("phone")int Phone,@Field("voketId")String voketId);
}
