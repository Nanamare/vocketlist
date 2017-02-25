package com.vocketlist.android.net.baseservice;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-21.
 */

public interface RoomService {

	//커뮤니티 조회
	@GET("room/list")
	Observable<Response<ResponseBody>> getVoketList();

	//글 좋아요
	@FormUrlEncoded
	@PUT("rooms/isLike")
	Observable<Response<ResponseBody>> updateIsLike(@Field("token") String token
			, @Field("isLike") boolean isLike
			, @Field("roomId") String roomId);

	//댓글 달기
	@FormUrlEncoded
	@POST("rooms/comment")
	Observable<Response<ResponseBody>> addComment(@Field("token") String token
			, @Field("comment") String comment
			, @Field("roomId") String roomId);

	//댓글 삭제
	@FormUrlEncoded
	@DELETE("rooms/deleteComment")
	Observable<Response<ResponseBody>> deleteComment(@Field("token") String token
			, @Field("commentId") String commentId
			, @Field("roomId") String roomId);
	
	//이미지 상세 보기
	@GET("rooms/imgDetail")
	Observable<Response<ResponseBody>> getDetailImg(@Query("token") String token);

	//커뮤니티 글작성
	@FormUrlEncoded
	@POST("rooms/create")
	Observable<Response<ResponseBody>> addRoom(@Part MultipartBody.Part file
			, @Field("token") String token, @Field("content") String content
			, @Field("voketType") String voketType, @Field("date") String date);


}
