package com.vocketlist.android.api.comment.model;

import com.google.gson.annotations.SerializedName;
import com.vocketlist.android.api.community.model.User;

import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 4. 8..
 */

public class CommentDetailModel {
    @SerializedName("id") public int mCommentId;
    @SerializedName("user") public User mUserInfo;
    @SerializedName("content") public String mContent;
    @SerializedName("reply_count") public int mReplyCount;
    @SerializedName("replies") public List<Comment> mReplyComment;
    @SerializedName("timestamp") public String mTimestamp;

    public static class Comment {
        @SerializedName("id") public int mCommentId;
        @SerializedName("user") public User mUserInfo;
        @SerializedName("content") public String mContent;
        @SerializedName("timestamp") public String mTimestamp;
    }
}
