package com.vocketlist.android.api.community.model;

import com.google.gson.annotations.SerializedName;
import com.vocketlist.android.api.Link;
import com.vocketlist.android.api.comment.model.CommentListModel;
import com.vocketlist.android.api.vocket.VolunteerDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */

public class CommunityList implements Serializable {
    @SerializedName("page_count") public int mPageCnt;
    @SerializedName("count") public int mCount;
    @SerializedName("data") public List<CommunityData> mData;
    @SerializedName("page_size") public int mPageSize;
    @SerializedName("links") public Link mLinks;
    @SerializedName("page_current") public int mPageCurrentCnt;

    public static class CommunityData implements Serializable  {
        @SerializedName("id") public int mId;
        @SerializedName("author") public User mUser;
        @SerializedName("image") public String mImageUrl;
        @SerializedName("origin_image") public String mOriginImage;
        @SerializedName("content") public String mContent;
        @SerializedName("like_count") public int mLikeCount;
        @SerializedName("like") public boolean mIsLike;
        @SerializedName("comments") public ArrayList<CommentListModel.Comment> mComment;
        @SerializedName("updated") public String mUpdateDate;
        @SerializedName("created") public String mCreateDate;
        @SerializedName("service") public VolunteerDetail mService;
    }
}
