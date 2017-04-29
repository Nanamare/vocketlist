package com.vocketlist.android.api.schedule;

import com.vocketlist.android.api.BaseServiceErrorChecker;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;

import retrofit2.Response;
import rx.Observable;

import static com.vocketlist.android.api.ServiceDefine.retrofit;

/**
 * Created by SeungTaek.Lim on 2017. 3. 5..
 */

public final class ScheduleServiceManager {
    private static ScheduleService service = retrofit.create(ScheduleService.class);

    private ScheduleServiceManager() {

    }

    public static Observable<Response<BaseResponse<ScheduleModel>>> getScheduleList(){
        return  service
                .getScheduleList()
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<>()));
    }
}
