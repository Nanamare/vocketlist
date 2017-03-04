package com.vocketlist.android.api.vocket.error;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.vocket.Volunteer;
import com.vocketlist.android.network.service.ErrorChecker;

/**
 * Created by kinamare on 2017-02-21.
 */

public class VocketErrorChecker implements ErrorChecker<BaseResponse<Volunteer>> {

	@Override
	public void checkError(BaseResponse<Volunteer> data) throws RuntimeException {
		if (data == null) {
			throw new VocketError(data);
		}


	}
}
