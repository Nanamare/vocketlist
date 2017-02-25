package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.network.service.ErrorChecker;

import okhttp3.ResponseBody;

/**
 * Created by kinamare on 2017-02-26.
 */

public class ApplyVolunteerErrorChecker implements ErrorChecker<ResponseBody> {
	@Override
	public void checkError(ResponseBody data) throws RuntimeException {
		if (data == null) {
			throw new ApplyVolunteerError();
		}
	}
}
