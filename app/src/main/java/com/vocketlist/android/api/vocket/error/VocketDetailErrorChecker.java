package com.vocketlist.android.api.vocket.error;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.api.vocket.VolunteerDetail;
import com.vocketlist.android.network.service.ErrorChecker;

/**
 * Created by kinamare on 2017-02-22.
 */

public class VocketDetailErrorChecker implements ErrorChecker<BaseResponse<VolunteerDetail>> {


	@Override
	public void checkError(BaseResponse<VolunteerDetail> data) throws RuntimeException {

		if (data == null) {
			throw new VocketDetailError(data);
		}


	}
}
