package com.vocketlist.android.api.address;

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
 * Created by SeungTaek.Lim on 2017. 4. 29..
 */

    public final class AddressServiceManager {
    private static final AddressService SERVICE = ServiceDefine.retrofit.create(AddressService.class);

    public static final Observable<Response<BaseResponse<List<AddressFirstInfo>>>> getFirstAddress() {
        return SERVICE.getFirstAddress()
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<List<AddressFirstInfo>>()));
    }

    public static final Observable<Response<BaseResponse<List<AddressSecondInfo>>>> getSecondAddress(int id) {
        return SERVICE.getSecondAddress(id)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<List<AddressSecondInfo>>()));
    }
}
