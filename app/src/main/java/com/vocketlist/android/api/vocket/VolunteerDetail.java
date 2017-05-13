package com.vocketlist.android.api.vocket;

import com.google.gson.annotations.SerializedName;
import com.vocketlist.android.defined.Category;

import java.io.Serializable;

/**
 * Created by kinamare on 2017-02-22.
 */
//봉사 상세페이지
public class VolunteerDetail implements Serializable {
	@SerializedName("id") public int mId;
	@SerializedName("title") public String mTitle;
	@SerializedName("start_date") public String mStartDate;
	@SerializedName("end_date") public String mEndDate;
	@SerializedName("recruit_start_date") public String mRecruitStartDate;
	@SerializedName("recruit_end_date") public String mRecruitEndDate;
	@SerializedName("start_time") public String mStartTime;
	@SerializedName("end_time") public String mEndTime;
	@SerializedName("active_day") public  String mActiveDay;
	@SerializedName("organization") public String mHostName;
	@SerializedName("place") public String mPlace;
	@SerializedName("content") public String mContent;
	@SerializedName("is_active") public boolean mIsActive;
	@SerializedName("is_direct") public boolean mIsDirect;
	@SerializedName("first_category") public Category mFirstCategory;
	@SerializedName("second_category") public String mSecondCategory;
	@SerializedName("first_register_office") public String mFirstRegisterOffice;
	@SerializedName("second_register_office") public String mSecondRegisterOffice;
	@SerializedName("recruit_num_by_day") public int mNumByDay;
	@SerializedName("image") public String mImageUrl;
	@SerializedName("organization_id") public int mOrganzationId;
	@SerializedName("url") public String mInternalLinkUrl;
	@SerializedName("is_participate") public boolean mIsParticipate;
}
