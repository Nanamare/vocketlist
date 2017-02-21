package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.network.service.ErrorChecker;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by kinamare on 2017-02-21.
 */

public class VoketDetailErrorChecker implements ErrorChecker<ResponseBody> {
	ServiceManager manager;
	@Override
	public void checkError(ResponseBody data) throws RuntimeException {
		if (data == null) {
			throw new VoketDetailError();
		}
		manager = new ServiceManager();

		try {
			Boolean result = manager.getStatusResult(data.string());
			if (!result) {
				throw new VoketDetailError();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new VoketDetailError(e.toString());
		}


	}
}
