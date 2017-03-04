package com.vocketlist.android.api.errorchecker;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.VolunteerDetail;

/**
 * Created by kinamare on 2017-02-22.
 */

public class VoketDetailError extends RuntimeException{
	private BaseResponse<VolunteerDetail> mData;

	public VoketDetailError(BaseResponse<VolunteerDetail> data) {
		super();
		mData = data;
	}
}
