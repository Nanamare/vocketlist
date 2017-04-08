package com.vocketlist.android.api.notice;

import com.vocketlist.android.api.BaseServiceErrorChecker;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 4. 8..
 */

public class NoticeServiceManager {
    private static NoticeService SERVICE = ServiceDefine.retrofit.create(NoticeService.class);

    private NoticeServiceManager() {

    }

    public Observable<Response<BaseResponse<NoticeModel>>> getNotice() {
        return SERVICE.notice()
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<NoticeModel>()));
    }
}
