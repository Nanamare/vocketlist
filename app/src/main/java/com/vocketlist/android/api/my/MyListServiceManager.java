package com.vocketlist.android.api.my;

import com.vocketlist.android.api.BaseServiceErrorChecker;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;

import java.util.List;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 4. 22..
 */

public final class MyListServiceManager {
    private static final int DEFAULT_PAGE_SIZE = 20;

    private static final MyListService SERVICE = ServiceDefine.retrofit.create(MyListService.class);

    public static Observable<Response<BaseResponse<MyListContent>>> write(String content, boolean isDone) {
        return SERVICE.write(content, isDone)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<MyListContent>()));
    }

    public static Observable<Response<BaseResponse<MyListContent>>> modify(int id, String content, boolean isDone) {
        return SERVICE.modify(id, content, isDone)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<MyListContent>()));
    }

    public static Observable<Response<BaseResponse<List<MyListContent>>>> get(int page) {
        return SERVICE.get(page, DEFAULT_PAGE_SIZE)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<List<MyListContent>>()));
    }

    public static Observable<Response<BaseResponse<Void>>> delete(int id) {
        return SERVICE.delete(id)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<Void>()));
    }
}
