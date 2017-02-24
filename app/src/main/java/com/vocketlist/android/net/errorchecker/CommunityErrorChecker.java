package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.network.service.ErrorChecker;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by kinamare on 2017-02-24.
 */

public class CommunityErrorChecker  implements ErrorChecker<ResponseBody> {

	ServiceManager manager = new ServiceManager();

	@Override
	public void checkError(ResponseBody data) throws RuntimeException {
		if (data == null) {
			throw new CommunityError();
		}

		try {
			Boolean result = manager.getStatusResult(data.string());
			if (!result) {
				throw new FcmRegisterError();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new CommunityError(e.toString());
		}
	}
}
