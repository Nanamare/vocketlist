package com.vocketlist.android.dto;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * 데이터 : 봉사활동리스트 : 더미
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class Volunteer implements Serializable {


	@SerializedName("page_count") public int mPageCount;
	@SerializedName("page_current") public int mPageCurrent;
	@SerializedName("links") public Link mLink;
	@SerializedName("data") public List<Data> mDataList;
	@SerializedName("count") public int mCount;
	@SerializedName("page_size") public int mPageSize;
	public static class Link {
		@SerializedName("next") public String next;
		@SerializedName("previous") public String previous;
	}

	public static class Data implements Serializable {
		@SerializedName("is_active") public boolean mIsActive;
		@SerializedName("id") public int mId;
		@SerializedName("organization_id") public int mOrganizationId;
		@SerializedName("title") public String mTitle;
		@SerializedName("start_date") public String mStartDate;
		@SerializedName("first_register_office") public String mFirstOffice;
		@SerializedName("image")public String imgUrl;
	}



}
