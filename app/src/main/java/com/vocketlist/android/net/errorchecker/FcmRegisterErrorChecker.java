package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.network.service.ErrorChecker;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by kinamare on 2017-02-20.
 */

public class FcmRegisterErrorChecker implements ErrorChecker<ResponseBody> {

	ServiceManager manager = new ServiceManager();

	@Override
	public void checkError(ResponseBody data) throws RuntimeException {
		if (data == null) {
			throw new FcmRegisterError();
		}

		try {
			Boolean result = manager.getStatusResult(data.string());
			if (!result) {
				throw new FcmRegisterError();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new FcmRegisterError(e.toString());
		}
	}
}
