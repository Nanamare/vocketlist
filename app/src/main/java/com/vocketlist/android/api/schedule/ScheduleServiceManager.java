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
    private static ScheduleService SERVICE = retrofit.create(ScheduleService.class);

    private ScheduleServiceManager() {

    }

    public static Observable<Response<BaseResponse<ScheduleListModel>>> list(){
        return  SERVICE
                .list()
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<>()));
    }

    public static Observable<Response<BaseResponse<ScheduleWriteModel>>> write(String title,
                                                                               String startDate,
                                                                               String endDate,
                                                                               String startTime,
                                                                               String endTime,
                                                                               String place,
                                                                               boolean isDone) {
        return SERVICE.write(title, startDate, endDate, startTime, endTime, place, isDone)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<>()));
    }

    public static Observable<Response<BaseResponse<ScheduleDeleteModel>>> delete(int id) {
        return SERVICE.delete(id)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM));
    }

    public static Observable<Response<BaseResponse<ScheduleModel>>> detail(int id) {
        return SERVICE.detail(id)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<>()));
    }

    public static Observable<Response<BaseResponse<ScheduleWriteModel>>> modify(String title,
                                                                                String startDate,
                                                                                String endDate,
                                                                                String startTime,
                                                                                String endTime,
                                                                                String place,
                                                                                boolean isDone) {
        return SERVICE.modify(title, startDate, endDate, startTime, endTime, place, isDone)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<>()));
    }
}
