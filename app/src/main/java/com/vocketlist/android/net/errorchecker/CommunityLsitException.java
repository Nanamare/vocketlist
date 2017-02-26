package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.community.CommunityList;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */
public class CommunityLsitException extends RuntimeException {
    private BaseResponse<CommunityList> mData;

    public CommunityLsitException(BaseResponse<CommunityList> data) {
        mData = data;
    }
}
