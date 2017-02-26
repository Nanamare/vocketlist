package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.community.CommunityList;
import com.vocketlist.android.network.service.ErrorChecker;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */
public class CommunityListErrorChecker implements ErrorChecker<BaseResponse<CommunityList>> {
    @Override
    public void checkError(BaseResponse<CommunityList> data) throws RuntimeException {
        if (data.mSuccess == false ) {
            throw new CommunityLsitException(data);
        }
    }
}
