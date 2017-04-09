package com.vocketlist.android.api.comment.model;

import com.google.gson.annotations.SerializedName;
import com.vocketlist.android.api.Link;
import com.vocketlist.android.api.community.model.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 4. 8..
 */

public class CommentListModel {
    @SerializedName("data") public List<Comment> mCommentList;
    @SerializedName("links") public Link mLink;
    @SerializedName("page_size") public int mPageSize;
    @SerializedName("count") public int mCount;
    @SerializedName("page_current") public int mPageIndex;
    @SerializedName("page_count") public int mPageCount;

    public static class Comment implements Serializable{
        @SerializedName("id") public int mCommentId;
        @SerializedName("user") public User mUserInfo;
        @SerializedName("content") public String mContent;
        @SerializedName("reply_count") public int mReplyCount;
        @SerializedName("timestamp") public String mTimestamp;
    }
}
