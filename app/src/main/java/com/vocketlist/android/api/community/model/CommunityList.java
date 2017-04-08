package com.vocketlist.android.api.community.model;

import com.google.gson.annotations.SerializedName;
import com.vocketlist.android.api.Link;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */

public class CommunityList implements Serializable {
    @SerializedName("count") public int mCount;
    @SerializedName("page_count") public int mPageCount;
    @SerializedName("page_current") public int mPageNumber;
    @SerializedName("page_size") public int mPageSize;
    @SerializedName("links") public Link mLinks;
    @SerializedName("data") public List<CommunityData> mData;

    public static class CommunityData implements Serializable  {
        @SerializedName("id") public int mId;
        @SerializedName("author") public User mUser;
        @SerializedName("image") public String mImageUrl;
        @SerializedName("content") public String mContent;
        @SerializedName("updated") public String mUpdateDate;
        @SerializedName("created") public String mCreateDate;
    }
}
