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

    private static List<FirstAddress> sFirstAddress = new ArrayList<>();
    private static Map<String, List<AddressModel.SecondAddress>> sSecondAddress = new HashMap<String, List<AddressModel.SecondAddress>>();

    public static final Observable<Response<BaseResponse<List<AddressModel>>>> getsFirstAddress() {
        return SERVICE.getFirstAddress()
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<List<AddressModel>>()))
                .map(new Func1<Response<BaseResponse<List<AddressModel>>>, Response<BaseResponse<List<AddressModel>>>>() {
                    @Override
                    public Response<BaseResponse<List<AddressModel>>> call(Response<BaseResponse<List<AddressModel>>> baseResponseResponse) {
                        sFirstAddress.clear();
                        sSecondAddress.clear();

                        for (AddressModel addressModel : baseResponseResponse.body().mResult) {
                            sFirstAddress.add(new FirstAddress(addressModel.mId, addressModel.mAddressName));
                            sSecondAddress.put(addressModel.mAddressName, addressModel.mSecondAddress);
                        }

                        return baseResponseResponse;
                    }
                });
    }

    public static void refreshAddress() {
        getsFirstAddress().subscribe(new EmptySubscriber<Response<BaseResponse<List<AddressModel>>>>());
    }

    public static List<FirstAddress> getFirstAddressList() {
        return sFirstAddress;
    }

    public static List<AddressModel.SecondAddress> getSecondAddress(String firstAddressName) {
        return sSecondAddress.get(firstAddressName);
    }
}
