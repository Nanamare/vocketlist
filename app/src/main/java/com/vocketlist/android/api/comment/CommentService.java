package com.vocketlist.android.api.comment;

import com.vocketlist.android.api.comment.model.CommentDetailModel;
import com.vocketlist.android.api.comment.model.CommentListModel;
import com.vocketlist.android.api.comment.model.CommentWriteModel;
import com.vocketlist.android.dto.BaseResponse;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 4. 8..
 */

public interface CommentService {
    @GET("comments/")
    Observable<Response<BaseResponse<CommentListModel>>> list(@Query("post_id") int postId,
                                                              @Query("page") int pageNum,
                                                              @Query("page_size") int pageSize);

    @FormUrlEncoded
    @POST("comments/")
    Observable<Response<BaseResponse<CommentWriteModel>>> write(@Field("post_id") int postId,
                                                                @Field("content") String content);

    @FormUrlEncoded
    @POST("comments/")
    Observable<Response<BaseResponse<CommentWriteModel>>> write(@Field("post_id") int postId,
                                                                @Field("parent_id") int parentCommentId,
                                                                @Field("content") String content);

    @DELETE("comments/{id}/")
    Observable<Response<BaseResponse<Void>>> delete(@Path("id") int commentId);

    @GET("comments/{id}/")
    Observable<Response<BaseResponse<CommentDetailModel>>> detail(@Path("id") int commentId);

    @FormUrlEncoded
    @PATCH("comments/{id}/")
    Observable<Response<BaseResponse<CommentWriteModel>>> modify(@Path("id") int commentId,
                                                                 @Field("post_id") int postId,
                                                                 @Field("content") String content);

    @FormUrlEncoded
    @PATCH("comments/{id}/")
    Observable<Response<BaseResponse<CommentWriteModel>>> modify(@Path("id") int commentId,
                                                                 @Field("post_id") int postId,
                                                                 @Field("parent_id") int parentCommentId,
                                                                 @Field("content") String content);
}
