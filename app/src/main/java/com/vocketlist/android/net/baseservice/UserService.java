package com.vocketlist.android.net.baseservice;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by kinamare on 2017-02-20.
 */

public interface UserService {
	@FormUrlEncoded
	@PUT("/fcm/register")
	Observable<ResponseBody> registerToken(@Field("token") String token);

}
