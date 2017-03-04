package com.vocketlist.android.api.my;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-21.
 */

public interface MyListService {

	@GET("myList")
	Observable<Response<ResponseBody>> getMyList(@Query("token") String token);

	@PUT("myList/isDone")
	Observable<Response<ResponseBody>> updateIsDone(@Field("token") String token
			, @Field("myListId") String myListId, @Field("isDone") boolean isDone);

	@POST("myList/create")
	Observable<Response<ResponseBody>> addNewVocketList(@Field("token") String token
			, @Field("content") String content, @Field("isDone") boolean isDone
			, @Field("date") String date);
}
