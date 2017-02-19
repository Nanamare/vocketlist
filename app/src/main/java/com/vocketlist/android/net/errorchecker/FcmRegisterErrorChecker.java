package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.network.service.ErrorChecker;

/**
 * Created by kinamare on 2017-02-20.
 */

public class FcmRegisterErrorChecker implements ErrorChecker<String> {
	@Override
	public void checkError(String data) throws RuntimeException {
		if (data == null) {
			throw new FcmRegisterError();
		}

	}
}
