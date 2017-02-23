package com.vocketlist.android.net.errorchecker;

/**
 * Created by kinamare on 2017-02-24.
 */

public class CommunityError extends RuntimeException{
	public CommunityError (){
		super("Community NULL ERROR");
	}
	CommunityError(String error){
		super(error);
	}
}
