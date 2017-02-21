package com.vocketlist.android.net.baseservice;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-21.
 */

public interface VoketService {

	@GET("services/list")
	Observable<Response<ResponseBody>> getVoketList(@Query("token") String token);

	@GET("service/detail")
	Observable<Response<ResponseBody>> getVoketDetailList(@Query("token") String token);
}
