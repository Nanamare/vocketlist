package com.vocketlist.android.api.schedule;

import com.google.gson.annotations.SerializedName;
import com.vocketlist.android.api.Link;

import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 4. 29..
 */

public class ScheduleListModel {
    @SerializedName("links") public Link mLink;
    @SerializedName("count") public int mCount;
    @SerializedName("page_count") public int mPageCount;
    @SerializedName("page_current") public int mPageCurrent;
    @SerializedName("page_size") public int mPageSize;

    @SerializedName("data") public List<Schedule> mScheduleList;

    public static class Schedule {
        @SerializedName("id") public int mId;
        @SerializedName("is_done") public boolean mIsDone;
        @SerializedName("title") public String mTitle;
        @SerializedName("start_date") public String mStartDate;
        @SerializedName("end_date") public String mEndDate;
        @SerializedName("start_time") public String mStartTime;
        @SerializedName("end_time") public String mEndTime;
        @SerializedName("place") public String mArea;
    }
}
