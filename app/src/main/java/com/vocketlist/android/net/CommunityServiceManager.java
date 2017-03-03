package com.vocketlist.android.net;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.community.CommunityDetail;
import com.vocketlist.android.dto.community.CommunityList;
import com.vocketlist.android.net.baseservice.CommunityService;
import com.vocketlist.android.net.errorchecker.CommunityListErrorChecker;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */

public class CommunityServiceManager {
    private static final int DEAULT_PAGE_SIZE = 20;

    private static CommunityService service = ServiceManager.retrofit.create(CommunityService.class);

    public static Observable<Response<BaseResponse<CommunityList>>> list(int pageNo) {
        return service.list(pageNo, DEAULT_PAGE_SIZE)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new CommunityListErrorChecker()));
    }

    public static Observable<Response<BaseResponse<CommunityDetail>>> detail(int id) {
        return service.detail(id)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new CommunityDetailErrorChecker()));
    }
}
