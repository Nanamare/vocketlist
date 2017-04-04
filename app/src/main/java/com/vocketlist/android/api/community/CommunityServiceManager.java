package com.vocketlist.android.api.community;

import com.vocketlist.android.api.BaseServiceErrorChecker;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.api.community.model.CommunityDetail;
import com.vocketlist.android.api.community.model.CommunityLike;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */

public final class CommunityServiceManager {
    private static final int DEAULT_PAGE_SIZE = 20;

    private static CommunityService service = ServiceDefine.retrofit.create(CommunityService.class);

    private CommunityServiceManager() {

    }

    public static Observable<Response<BaseResponse<CommunityList>>> list(int pageNo) {
        return service.list(pageNo, DEAULT_PAGE_SIZE)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommunityList>()));
    }

    public static Observable<Response<BaseResponse<CommunityDetail>>> detail(int postId) {
        return service.detail(postId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommunityDetail>()));
    }

    public static Observable<Response<BaseResponse<CommunityLike>>> like(int postId) {
        return service.like(postId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommunityLike>()));
    }

//    public static Observable<Response<BaseResponse<CommunityWrite>>> write(int vocketServiceId, String imagePath, String content) {
//        File file = new File(imagePath);
//
//        RequestBody requestFile =
//                RequestBody.create(
//                        MediaType.parse(getContentResolver().getType(file)),
//                        file
//                );
//
//
//        return service.write(vocketServiceId, imagePath, content)
//                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
//                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommunityLike>()));
//    }

    public static Observable<Response<BaseResponse<Void>>> delete(int postId) {
        return service.delete(postId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<Void>()));
    }

}
