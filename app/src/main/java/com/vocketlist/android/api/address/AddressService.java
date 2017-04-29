package com.vocketlist.android.api.address;

import com.vocketlist.android.dto.BaseResponse;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 4. 29..
 */

public interface AddressService {
    @GET("services/address/first/")
    Observable<Response<BaseResponse<List<AddressFirstInfo>>>> getFirstAddress();

    @GET("services/address/second/")
    Observable<Response<BaseResponse<List<AddressSecondInfo>>>> getSecondAddress(@Query("first_id") int id);
}
