package com.vocketlist.android.api.community;

import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.community.model.CommunityDetail;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.api.BaseServiceErrorChecker;
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

    public static Observable<Response<BaseResponse<CommunityDetail>>> detail(int id) {
        return service.detail(id)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<CommunityDetail>()));
    }
}
