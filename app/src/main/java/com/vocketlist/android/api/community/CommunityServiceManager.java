package com.vocketlist.android.api.community;

import com.vocketlist.android.api.BaseServiceErrorChecker;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.api.community.model.CommunityLike;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.api.community.model.CommunityWrite;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

//    public static Observable<Response<BaseResponse<CommunityList>>> list(int pageNo, String userName) {
//        return search(pageNo, userName);
//    }

    public static Observable<Response<BaseResponse<CommunityList>>> search(int pageNo, String userId, String searchKeyword) {
        return service.list(pageNo, DEAULT_PAGE_SIZE, userId, searchKeyword)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommunityList>()));
    }

    public static Observable<Response<BaseResponse<CommunityList.CommunityData>>> detail(int postId) {
        return service.detail(postId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommunityList.CommunityData>()));
    }

    public static Observable<Response<BaseResponse<CommunityWrite>>> write(int vocketServiceId, String imagePath, String content) {
        MultipartBody.Part image = null;
        RequestBody description = null;
        RequestBody serviceId = null;

        if(imagePath != null){
            image = getMultipartBody(imagePath);
        }

        if (content != null) {
            description = RequestBody.create(okhttp3.MultipartBody.FORM, content);
        }

        if (vocketServiceId > 0) {
            serviceId = RequestBody.create(okhttp3.MultipartBody.FORM, Integer.toString(vocketServiceId));
        }

        return service.write(image, description, serviceId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommunityWrite>()));
    }

    public static Observable<Response<BaseResponse<CommunityWrite>>> modify(int communityId, int vocketServiceId, String imagePath, String content) {
        RequestBody contentId;
        MultipartBody.Part image = null;
        RequestBody description = null;
        RequestBody serviceId = null;

//        contentId = RequestBody.create(okhttp3.MultipartBody.FORM, Integer.toString(communityId));

        if(imagePath != null){
            image = getMultipartBody(imagePath);
        }

        if (content != null) {
            description = RequestBody.create(okhttp3.MultipartBody.FORM, content);
        }

        if (vocketServiceId > 0) {
            serviceId = RequestBody.create(okhttp3.MultipartBody.FORM, Integer.toString(vocketServiceId));
        }

        return service.modify(communityId, image, description, serviceId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommunityWrite>()));
    }

    private static MultipartBody.Part getMultipartBody(String imagePath) {
        if (imagePath == null) {
            return null;
        }

        File file = new File(imagePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        return body;
    }

    public static Observable<Response<BaseResponse<Void>>> delete(int postId) {
        return service.delete(postId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM));
    }

    public static Observable<Response<BaseResponse<CommunityLike>>> like(int postId) {
        return service.like(postId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommunityLike>()));
    }

}
