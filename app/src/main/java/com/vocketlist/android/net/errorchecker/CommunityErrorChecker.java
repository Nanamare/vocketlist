package com.vocketlist.android.net.errorchecker;

import com.vocketlist.android.dto.Post;
import com.vocketlist.android.dto.Volunteer;
import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.network.service.ErrorChecker;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by kinamare on 2017-02-24.
 */

public class CommunityErrorChecker  implements ErrorChecker<Post> {
	@Override
	public void checkError(Post data) throws RuntimeException {
		if (data == null) {
			throw new CommunityError(data);
		}

//		if (data.mLink == null) {
//			throw new CommunityError(data);
//		}

//		try {
//			Boolean result = manager.getStatusResult(data);
//			if (!result) {
//				throw new FcmRegisterError();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new CommunityError(e.toString());
//		}
	}
}
