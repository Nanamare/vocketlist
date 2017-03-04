package com.vocketlist.android.api.user.error;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.service.ErrorChecker;

/**
 * Created by kinamare on 2017-02-20.
 */

public class FcmRegisterErrorChecker implements ErrorChecker<BaseResponse<Boolean>> {


	@Override
	public void checkError(BaseResponse<Boolean> data) throws RuntimeException {
		if (data == null) {
			throw new FcmRegisterError();
		}

	}
}
