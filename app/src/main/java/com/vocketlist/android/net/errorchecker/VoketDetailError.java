package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.network.service.ErrorChecker;

import okhttp3.ResponseBody;

/**
 * Created by kinamare on 2017-02-21.
 */

public class VoketDetailError extends RuntimeException {
	public VoketDetailError() {
		super("get VoketDetailList Error");
	}
	public VoketDetailError(String error) {
		super(error);
	}
}
