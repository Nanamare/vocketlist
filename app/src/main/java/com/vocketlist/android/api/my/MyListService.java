package com.vocketlist.android.api.my;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-21.
 */

public interface MyListService {
//
//	@GET("myList")
//	Observable<Response<ResponseBody>> getMyList(@Query("token") String token);
//
//	@PUT("myList/isDone")
//	Observable<Response<ResponseBody>> updateIsDone(@Field("token") String token
//			, @Field("myListId") String myListId, @Field("isDone") boolean isDone);
//
//	@POST("myList/create")
//	Observable<Response<ResponseBody>> addNewVocketList(@Field("token") String token
//			, @Field("content") String content, @Field("isDone") boolean isDone
//			, @Field("date") String date);

	@POST("users/mylist/")
	Observable<Response<MyListContent>> write(String content, boolean isDone);

	@GET("users/mylist/")
	Observable<Response<List<MyListContent>>> get(int page, int pageSize);

	@DELETE("users/mylist/")
	Observable<Response<Void>> delete(int id);
}
