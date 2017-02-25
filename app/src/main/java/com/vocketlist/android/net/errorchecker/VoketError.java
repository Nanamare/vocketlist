package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Volunteer;

/**
 * Created by kinamare on 2017-02-21.
 */

public class VoketError extends RuntimeException {
	private BaseResponse<Volunteer> mData;

	public VoketError(BaseResponse<Volunteer> data) {
		super();
//		mData = data;
	}
}
