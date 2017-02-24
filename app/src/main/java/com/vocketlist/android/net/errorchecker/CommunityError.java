package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.dto.Post;
import com.vocketlist.android.dto.Volunteer;

/**
 * Created by kinamare on 2017-02-24.
 */

public class CommunityError extends RuntimeException{
	private final Post mData;

	public CommunityError (Post data){
		super();
		mData = data;
	}
}
