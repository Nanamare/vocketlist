package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.network.service.ErrorChecker;

import okhttp3.ResponseBody;

/**
 * Created by kinamare on 2017-02-21.
 */

public class VoketError extends RuntimeException {
	public VoketError() {
		super("VoketList Null Error");
	}
	public VoketError(String error) {
		super(error);
	}
}
