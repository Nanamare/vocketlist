package com.vocketlist.android.api.notification;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 4. 22..
 */

public final class NotificationModel {
    @SerializedName("subscribe") public List<NotificationInfo> mNotificationList;

    public class NotificationInfo {
        @SerializedName("type") public NotificationType mNotiType;
        @SerializedName("name") public String mName;
        @SerializedName("is_subscribe") public boolean mIsSubscribe;
    }
}
