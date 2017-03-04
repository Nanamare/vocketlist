package com.vocketlist.android.api.user.error;

/**
 * Created by kinamare on 2017-02-21.
 */

public class LoginFbException extends RuntimeException {
	public LoginFbException() {
		super("Login Fb Error");
	}
	public LoginFbException(String error) {
		super(error);
	}
}
