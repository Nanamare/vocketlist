package com.vocketlist.android.api.vocket.error;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.vocket.Volunteer;

/**
 * Created by kinamare on 2017-02-21.
 */

public class VocketError extends RuntimeException {
	private BaseResponse<Volunteer> mData;

	public VocketError(BaseResponse<Volunteer> data) {
		super();
//		mData = data;
	}
}
