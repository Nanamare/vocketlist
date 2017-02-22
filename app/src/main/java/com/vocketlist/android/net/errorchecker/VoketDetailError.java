package com.vocketlist.android.net.errorchecker;

/**
 * Created by kinamare on 2017-02-22.
 */

public class VoketDetailError extends RuntimeException{
	public VoketDetailError (){
		super("VoketDetail Null Error");
	}
	VoketDetailError(String error){
		super(error);
	}
}
