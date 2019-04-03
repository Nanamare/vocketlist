package com.vocketlist.android.api.notice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 공지사항
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 8.
 */
public class NoticeModel {
    @SerializedName("data") public List<Notice> mNoticeList;

    public static class Notice {
        @SerializedName("id") public int mId;
        @SerializedName("title") public String mTitle;
        @SerializedName("content") public String mContent;
        @SerializedName("image") public String mPhoto;
        @SerializedName("link") public String mLink;
        @SerializedName("timestamp") public String timestamp;
    }
}
