package com.vocketlist.android.api.notification;

import com.vocketlist.android.api.BaseServiceErrorChecker;
import com.vocketlist.android.api.ServiceDefine;
import com.vocketlist.android.dto.BaseResponse;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;
import com.vocketlist.android.preference.NotificationPreference;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SeungTaek.Lim on 2017. 4. 22..
 */

public final class NotificationServiceManager {
    private static NotificationService SERVICE = ServiceDefine.retrofit.create(NotificationService.class);

    private NotificationServiceManager() {

    }

    public static Observable<Response<BaseResponse<Void>>> setting(NotificationType type, boolean switchOn) {
        return SERVICE.setting(type, switchOn)
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<Void>()));
    }

    public static Observable<Response<BaseResponse<NotificationModel>>> getSetting() {
        return SERVICE.getSetting()
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<>(new BaseServiceErrorChecker<NotificationModel>()))
                .map(new Func1<Response<BaseResponse<NotificationModel>>, Response<BaseResponse<NotificationModel>>>() {
                    @Override
                    public Response<BaseResponse<NotificationModel>> call(Response<BaseResponse<NotificationModel>> baseResponseResponse) {
                        NotificationModel model = baseResponseResponse.body().mResult;

                        for (NotificationModel.NotificationInfo info : model.mNotificationList) {
                            if (NotificationType.NOTIFY == info.mNotiType) {
                                NotificationPreference.getInstance().setNotiSetting(info.mIsSubscribe);

                            } else if (NotificationType.RECOMMEND == info.mNotiType) {
                                NotificationPreference.getInstance().setRecommend(info.mIsSubscribe);

                            } else if (NotificationType.NEW_COMMUNITY == info.mNotiType) {
                                NotificationPreference.getInstance().setCommunity(info.mIsSubscribe);

                            } else if (NotificationType.NEW_SERVICE == info.mNotiType) {
                                NotificationPreference.getInstance().setNewVolunteer(info.mIsSubscribe);
                            }
                        }

                        return baseResponseResponse;
                    }
                });
    }
}
