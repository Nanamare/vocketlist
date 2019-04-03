package com.vocketlist.android.api.vocket.error;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.vocket.VolunteerDetail;

/**
 * Created by kinamare on 2017-02-22.
 */

public class VocketDetailError extends RuntimeException{
	private BaseResponse<VolunteerDetail> mData;

	public VocketDetailError(BaseResponse<VolunteerDetail> data) {
		super();
		mData = data;
	}
}
