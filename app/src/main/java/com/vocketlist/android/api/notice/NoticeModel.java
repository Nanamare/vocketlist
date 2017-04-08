package com.vocketlist.android.api.notice;

import com.google.gson.annotations.SerializedName;

/**
 * 공지사항
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 8.
 */
public class NoticeModel {
    @SerializedName("id") public Integer mId;
    @SerializedName("title") public String mTitle;
    @SerializedName("content") public String mContent;
    @SerializedName("link") public String mLink;
    @SerializedName("timestamp") public String timestamp;
}
