package com.vocketlist.android.net.baseservice;

import com.vocketlist.android.dto.BaseResponse;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;


/**
 * Created by kinamare on 2017-02-20.
 */

public interface UserService {
	@FormUrlEncoded
	@PUT("/api/users/fcm/register/")
	Observable<Response<BaseResponse<Boolean>>> registerToken(@Field("fcmToken") String token);

	@FormUrlEncoded
	@POST("/api/users/auth/facebook/")
	Observable<Response<ResponseBody>> loginFb(@Field("userInfo") String userInfo
			,@Field("token") String token, @Field("userId") String userId);

}
