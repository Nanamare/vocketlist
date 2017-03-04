package com.vocketlist.android.api.errorchecker;

import com.vocketlist.android.dto.BaseResponse;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */
public class BaseServiceException extends RuntimeException {
    private BaseResponse<?> mData;

    public BaseServiceException(BaseResponse<?> data) {
        mData = data;
    }

    public <T> T getData() {
        return (T) mData;
    }

    @Override
    public String getMessage() {
        if (mData != null
                && mData.mMessage != null) {
            return mData.mMessage;
        }

        return super.getMessage();
    }
}
