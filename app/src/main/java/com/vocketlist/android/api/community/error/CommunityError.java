package com.vocketlist.android.api.community.error;

import com.vocketlist.android.dto.Post;

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
