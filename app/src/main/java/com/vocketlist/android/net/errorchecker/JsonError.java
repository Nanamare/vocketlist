package com.vocketlist.android.net.errorchecker;

import java.util.List;

/**
 * Created by kinamare on 2017-02-20.
 */

public class JsonError extends RuntimeException {
	public JsonError() {
		super();
	}
	public JsonError(String tokenResult) {
		super("tokenResult error");
	}
}
