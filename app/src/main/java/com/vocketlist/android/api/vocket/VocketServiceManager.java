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

    public static Observable<Response<BaseResponse<Volunteer>>> getVocketList(int page) {
        return getVocketList(null, page);
    }

    private VocketServiceManager() {

    }

    public static Observable<Response<BaseResponse<Volunteer>>> getVocketList(Category category, int page) {
        return service
                .getVocketCategoryList(category, page)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<Volunteer>()));

    }

    public static Observable<Response<BaseResponse<VolunteerDetail>>> getVocketDetail(int vocketId) {
        return service
                .getVocketDetail(vocketId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<BaseResponse<VolunteerDetail>>(new BaseServiceErrorChecker()));
    }

    public static Observable<Response<Void>> applyVolunteer(String name, String phone, int service_id){

        return service
                .applyVolunteer(name, phone,service_id)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker()));

    }
}
