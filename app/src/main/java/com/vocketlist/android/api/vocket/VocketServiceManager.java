package com.vocketlist.android.api.vocket;

import com.vocketlist.android.api.BaseServiceErrorChecker;
import com.vocketlist.android.defined.Category;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;

import retrofit2.Response;
import rx.Observable;

import static com.vocketlist.android.api.ServiceDefine.retrofit;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */

public final class VocketServiceManager {
    private static VocketService service = retrofit.create(VocketService.class);

//    public static Observable<Response<BaseResponse<Volunteer>>> getVocketList(int page) {
//        return getVocketList(null, page);
//    }

    private VocketServiceManager() {

    }

//    public static Observable<Response<BaseResponse<Volunteer>>> getVocketList(Category category, int page) {
//        return search((Category.All == category) ? null : category,
//                null,
//                null,
//                0,
//                false,
//                null,
//                page);
//    }

    public static Observable<Response<BaseResponse<Volunteer>>> search(Category category,
                                                                       String startDate,
                                                                       String endDate,
                                                                       int secondAddressId,
                                                                       boolean useVocketFilter,
                                                                       String searchKeyword,
                                                                       int page) {
        return service
                .getVocketCategoryList((Category.All == category) ? null : category,
                        startDate,
                        endDate,
                        (secondAddressId > 0) ? Integer.toString(secondAddressId) : null,
                        (useVocketFilter) ? "vocket" : null,
                        page,
                        searchKeyword)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<Volunteer>()));
    }

    public static Observable<Response<BaseResponse<VolunteerDetail>>> getVocketDetail(int vocketId) {
        return service
                .getVocketDetail(vocketId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<BaseResponse<VolunteerDetail>>(new BaseServiceErrorChecker()));
    }

    // 봉사 신청 또는 신청 취소
    public static Observable<Response<BaseResponse<Participate>>> applyVolunteer(int service_id, String name, String phone){

        return service
                .participate(service_id, name, phone)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker()));

    }
}
