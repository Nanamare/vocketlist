package com.vocketlist.android.net.errorchecker;

import java.util.List;

/**
 * Created by kinamare on 2017-02-20.
 */

public class FcmRegisterError extends RuntimeException {
	public FcmRegisterError() {
		super("Fcm error");
	}
	public FcmRegisterError(String error) {
		super(error);
	}
}
