package com.vocketlist.android.net.baseservice;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-24.
 */

public interface CommunityService {

	@GET("rooms/list")
	Observable<Response<ResponseBody>> getCommunityList();
}
