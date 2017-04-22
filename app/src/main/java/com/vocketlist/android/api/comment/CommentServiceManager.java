package com.vocketlist.android.api.comment;

import com.vocketlist.android.api.BaseServiceErrorChecker;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.api.comment.model.CommentDetailModel;
import com.vocketlist.android.api.comment.model.CommentListModel;
import com.vocketlist.android.api.comment.model.CommentWriteModel;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 4. 8..
 */

public final class CommentServiceManager {
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final CommentService SERVICE = ServiceDefine.retrofit.create(CommentService.class);

    private CommentServiceManager() {

    }

    public static Observable<Response<BaseResponse<CommentListModel>>> list(int postId, int pageNum) {
        return SERVICE.list(postId, pageNum, DEFAULT_PAGE_SIZE)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommentListModel>()));
    }

    public static Observable<Response<BaseResponse<CommentWriteModel>>> write(int postId, int parentCommentId, String content) {
        Observable<Response<BaseResponse<CommentWriteModel>>> observable;

        if (parentCommentId > 0) {
            observable = SERVICE.write(postId, parentCommentId, content);
        } else {
            observable = SERVICE.write(postId, content);
        }

        return observable.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommentWriteModel>()));
    }

    public static Observable<Response<BaseResponse<Void>>> delete(int commentId) {
        return SERVICE.delete(commentId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<Void>()));
    }

    public static Observable<Response<BaseResponse<CommentDetailModel>>> detail(int commentId) {
        return SERVICE.detail(commentId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommentDetailModel>()));
    }

    public static Observable<Response<BaseResponse<CommentWriteModel>>> modify(int commentId, String content) {
        return modify(commentId, 0, content);
    }

    public static Observable<Response<BaseResponse<CommentWriteModel>>> modify(int commentId, int parentCommentId, String content) {
        Observable<Response<BaseResponse<CommentWriteModel>>> observable;

        if (parentCommentId > 0) {
            observable = SERVICE.modify(commentId, parentCommentId, content);
        } else {
            observable = SERVICE.modify(commentId, content);
        }

        return observable.subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommentWriteModel>()));
    }
}
