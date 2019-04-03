package com.vocketlist.android.api.community;

import com.vocketlist.android.api.community.model.CommunityLike;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.api.community.model.CommunityWrite;
import com.vocketlist.android.dto.BaseResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-21.
 */

interface CommunityService {
	//커뮤니티 리스트 조회
//	@GET("posts/")
//	Observable<Response<BaseResponse<CommunityList>>> list(@Query("page") int page,
//														   @Query("page_size") int pageSize,
//														   @Query(value = "search", encoded = true) String searchKeyWord);

	@GET("posts/")
	Observable<Response<BaseResponse<CommunityList>>> list(@Query("page") int page,
														   @Query("page_size") int pageSize,
														   @Query("user_id") String userId,
														   @Query(value = "search", encoded = true) String searchKeyWord);

	// 특정 커뮤니티 상세 조회
	@GET("posts/{id}/")
	Observable<Response<BaseResponse<CommunityList.CommunityData>>> detail(@Path("id") int id);

	//커뮤니티 글작성
	@Multipart
	@POST("posts/")
	Observable<Response<BaseResponse<CommunityWrite>>> write(@Part MultipartBody.Part image
											, @Part("content") RequestBody content
											, @Part("service_id") RequestBody serviceId);

	// 커뮤니티 글 수정
	@Multipart
	@PATCH("posts/{id}/")
	Observable<Response<BaseResponse<CommunityWrite>>> modify(@Path("id") int contentId,
													  @Part MultipartBody.Part image
													, @Part("content") RequestBody content
													, @Part("service_id") RequestBody serviceId);

	// 커뮤니티 글 삭제
	@DELETE("posts/{id}/")
	Observable<Response<BaseResponse<Void>>> delete(@Path("id") int postId);

	// 커뮤니티 좋아요 / 취소
	@POST("posts/like/{post_id}/")
	Observable<Response<BaseResponse<CommunityLike>>> like(@Path("post_id") int postId);
}
