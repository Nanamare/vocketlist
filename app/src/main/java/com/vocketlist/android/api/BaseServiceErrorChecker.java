package com.vocketlist.android.api;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.service.ErrorChecker;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */
public class BaseServiceErrorChecker<T> implements ErrorChecker<BaseResponse<T>> {
    @Override
    public void checkError(BaseResponse<T> data) throws RuntimeException {
        if (data == null) {
            throw new BaseServiceException();
        }

        if (data.mSuccess == false ) {
            throw new BaseServiceException(data);
        }
    }
}
