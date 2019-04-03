package com.vocketlist.android.api.notification;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SeungTaek.Lim on 2017. 4. 22..
 */

public enum NotificationType {
    @SerializedName("notify") NOTIFY,
    @SerializedName("recommend") RECOMMEND,
    @SerializedName("new_service") NEW_SERVICE,
    @SerializedName("new_community") NEW_COMMUNITY
}
