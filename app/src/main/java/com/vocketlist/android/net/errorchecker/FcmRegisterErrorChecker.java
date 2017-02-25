package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.network.service.ErrorChecker;

import java.io.IOException;

import okhttp3.ResponseBody;

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
