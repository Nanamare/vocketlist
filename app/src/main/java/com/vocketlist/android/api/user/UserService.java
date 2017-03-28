package com.vocketlist.android.api.user;

import com.vocketlist.android.dto.BaseResponse;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;


/**
 * Created by kinamare on 2017-02-20.
 */

interface UserService {
	@FormUrlEncoded
	@PUT("/api/users/fcm/register/")
	Observable<Response<BaseResponse<Void>>> registerToken(@Field("fcm_token") String token, @Field("device_id")String deviceId);

	@FormUrlEncoded
	@POST("/api/users/auth/facebook/")
	Observable<Response<BaseResponse<LoginModel>>> loginFb(@Field("userInfo") String userInfo, @Field("token") String token, @Field("userId") String userId);

	@FormUrlEncoded
	@PUT("/api/users/token/verify/")
	Observable<Response<BaseResponse<LoginModel>>> tokenVerify(@Field("token") String token);
}
