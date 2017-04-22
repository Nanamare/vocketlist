package com.vocketlist.android.api.notification;

import com.vocketlist.android.dto.BaseResponse;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 4. 22..
 */

public interface NotificationService {
    @FormUrlEncoded
    @POST("notifications/")
    Observable<Response<BaseResponse<Void>>> setting(@Field("type") NotificationType type,
                                                     @Field("is_switch_on") boolean switchOn);


    @GET("notifications/")
    Observable<Response<BaseResponse<NotificationModel>>> getSetting();
}
