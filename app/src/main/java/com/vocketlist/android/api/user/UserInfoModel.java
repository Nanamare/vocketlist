package com.vocketlist.android.api.user;

import com.google.gson.annotations.SerializedName;
import com.vocketlist.android.api.my.MyListModel;

/**
 * Created by SeungTaek.Lim on 2017. 5. 13..
 */

public class UserInfoModel {
    @SerializedName("id") public int mUserId;
    @SerializedName("schedule") public ScheduleInfo mScheduleInfo;
    @SerializedName("mylist") public MyListInfo mMyListInfo;

    public static class ScheduleInfo {
        @SerializedName("total_schedule") public int mTotalSchedule;
    }

}
