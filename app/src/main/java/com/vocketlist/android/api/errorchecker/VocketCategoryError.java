package com.vocketlist.android.api.errorchecker;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.dto.Volunteer;

/**
 * Created by kinamare on 2017-02-25.
 */

public class VocketCategoryError extends RuntimeException {
	private BaseResponse<Volunteer> mData;


	public VocketCategoryError(BaseResponse<Volunteer> mData){
		super();
	}
}
