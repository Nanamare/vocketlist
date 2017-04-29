package com.vocketlist.android.api.address;

import com.vocketlist.android.api.BaseServiceErrorChecker;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.EmptySubscriber;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SeungTaek.Lim on 2017. 4. 29..
 */

public final class AddressServiceManager {
    private static final AddressService SERVICE = ServiceDefine.retrofit.create(AddressService.class);

    private static List<String> sFirstAddress = new ArrayList<>();
    private static Map<String, List<AddressInfo.SecondAddress>> sSecondAddress = new HashMap<String, List<AddressInfo.SecondAddress>>();

    public static final Observable<Response<BaseResponse<List<AddressInfo>>>> getsFirstAddress() {
        return SERVICE.getFirstAddress()
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<List<AddressInfo>>()))
                .map(new Func1<Response<BaseResponse<List<AddressInfo>>>, Response<BaseResponse<List<AddressInfo>>>>() {
                    @Override
                    public Response<BaseResponse<List<AddressInfo>>> call(Response<BaseResponse<List<AddressInfo>>> baseResponseResponse) {
                        sFirstAddress.clear();
                        sSecondAddress.clear();

                        for (AddressInfo addressInfo : baseResponseResponse.body().mResult) {
                            sFirstAddress.add(addressInfo.mAddressName);
                            sSecondAddress.put(addressInfo.mAddressName, addressInfo.mSecondAddress);
                        }

                        return baseResponseResponse;
                    }
                });
    }

    public static void refreshAddress() {
        getsFirstAddress().subscribe(new EmptySubscriber<Response<BaseResponse<List<AddressInfo>>>>());
    }

    public static List<String> getFirstAddressList() {
        return sFirstAddress;
    }

    public static List<AddressInfo.SecondAddress> getSecondAddress(String firstAddressName) {
        return sSecondAddress.get(firstAddressName);
    }
}
