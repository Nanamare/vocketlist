package com.vocketlist.android.net.errorchecker;

/**
 * Created by kinamare on 2017-02-21.
 */

public class LoginFbError extends RuntimeException {
	public LoginFbError() {
		super("Login Fb Error");
	}
	public LoginFbError(String error) {
		super(error);
	}
}
