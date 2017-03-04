package com.vocketlist.android.api.community;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.community.model.CommunityDetail;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.api.community.model.Modify;

import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-21.
 */

public interface CommunityService {
	//커뮤니티 리스트 조회
	@GET("posts/")
	Observable<Response<BaseResponse<CommunityList>>> list(@Query("page") int page,
															  @Query("page_size") int pageSize);

	// 특정 커뮤니티 상세 조회
	@GET("posts/{id}/")
	Observable<Response<BaseResponse<CommunityDetail>>> detail(@Path("id") int id);

	//커뮤니티 글작성
	@FormUrlEncoded
	@POST("posts/")
	Observable<Response<BaseResponse<Void>>> write(@Part MultipartBody.Part image
											, @Field("content") String content
											, @Field("voketType") String voketxType);

//	글 좋아요 / 취소
//	@POST("posts/like/{post_id}")
//	Observable<Response<BaseResponse<>>> like(@Path("post_id") String postId);

	// 등록된 글 수정
	@PUT("posts/{id}/")
	Observable<Response<BaseResponse<Modify>>> modify(@Path("id") String id,
													  @Part MultipartBody.Part image,
													  @Field("content") String content);

//	 커뮤니티 삭제
//	@DELETE("posts/{id}/")
//	Observable<Response<BaseResponse<>>> deleteComment(@Path("id") String id);
}
