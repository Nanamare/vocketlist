package com.vocketlist.android.api.community.error;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.community.model.CommunityDetail;
import com.vocketlist.android.network.service.ErrorChecker;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */
public class CommunityDetailErrorChecker implements ErrorChecker<BaseResponse<CommunityDetail>> {
    @Override
    public void checkError(BaseResponse<CommunityDetail> data) throws RuntimeException {

    }
}
