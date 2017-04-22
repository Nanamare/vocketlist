package com.vocketlist.android.api.notification;

import com.vocketlist.android.dto.BaseResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 4. 22..
 */

public interface NotificationService {
    @POST("notifications/")
    Observable<Response<BaseResponse<Void>>> setting(@Path("type") NotificationType type,
                                                     @Path("is_switch_on") boolean switchOn);


    @GET("notifications/")
    Observable<Response<BaseResponse<NotificationModel>>> getSetting();
}
