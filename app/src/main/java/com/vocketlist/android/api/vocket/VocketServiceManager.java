package com.vocketlist.android.api.vocket;

import com.vocketlist.android.api.errorchecker.ApplyVolunteerErrorChecker;
import com.vocketlist.android.api.vocket.error.VocketCategoryErrorChecker;
import com.vocketlist.android.api.vocket.error.VoketDetailErrorChecker;
import com.vocketlist.android.api.vocket.error.VoketErrorChecker;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.dto.VolunteerDetail;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

import static com.vocketlist.android.api.ServiceManager.retrofit;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */

public class VocketServiceManager {
    private static VocketService service = retrofit.create(VocketService.class);

    public static Observable<Response<BaseResponse<Volunteer>>> getVocketList(int page) {
        return service
                .getVocketList(page)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<BaseResponse<Volunteer>>(new VoketErrorChecker()));
    }

    public static Observable<Response<BaseResponse<Volunteer>>> getVocketCategoryList(String category, int page) {
        return service
                .getVocketCategoryList(category, page)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new VocketCategoryErrorChecker()));

    }

    public static Observable<Response<BaseResponse<VolunteerDetail>>> getVocketDetail(int voketId) {
        return service
                .getVocketDetail(voketId)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<BaseResponse<VolunteerDetail>>(new VoketDetailErrorChecker()));
    }

    public static Observable<Response<ResponseBody>> applyVolunteer(String name, String phone, int service_id){

        return service
                .applyVolunteer(name, phone,service_id)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new ApplyVolunteerErrorChecker()));

    }
}
