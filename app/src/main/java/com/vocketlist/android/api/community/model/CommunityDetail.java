package com.vocketlist.android.api.community.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */
public class CommunityDetail {
    @SerializedName("id") public int mId;
    @SerializedName("author") public User mUser;
    @SerializedName("service") public Serivce mService;
    @SerializedName("image") public String mImageUrl;
    @SerializedName("content") public String mContent;
    @SerializedName("like_count") public int mLikeCount;
    @SerializedName("like") public boolean mIsLiked;
    @SerializedName("updated") public String mUpdated;
    @SerializedName("created") public String mCreated;

    public static class Serivce {
        @SerializedName("id") public int mId;
        @SerializedName("title") public String mTitle;
        @SerializedName("start_date") public String mStartDate;
        @SerializedName("end_date") public String mEndDate;
        @SerializedName("recruit_start_date") public String mRecuirStartEnd;
        @SerializedName("recruit_end_date") public String mRecruitEndEnd;
        @SerializedName("start_time") public String mStartTime;
        @SerializedName("end_time") public String mEndTime;
        @SerializedName("active_day") public String mActiveDay;
        @SerializedName("host_name") public String mHostName;
        @SerializedName("place") public String mPlace;
        @SerializedName("content") public String mContent;
        @SerializedName("is_active") public boolean mIsActive;
        @SerializedName("first_category") public String mFirestCategory;
        @SerializedName("second_category") public String mSecondCategory;
        @SerializedName("first_register_office") public String mFirstRegisterOffice;
        @SerializedName("second_register_office") public String mSecondRegisterOffice;
        @SerializedName("recruit_num_by_day") public int mRecruitNumByDay;
        @SerializedName("image") public String mImageUrl;
        @SerializedName("organization_id") public int mOrganiztionId;
        @SerializedName("url") public String mUrl;
        @SerializedName("is_participate") public boolean mIsParticipate;

    }
}
