package com.vocketlist.android.api.comment.model;

import com.google.gson.annotations.SerializedName;
import com.vocketlist.android.api.community.model.User;

/**
 * Created by SeungTaek.Lim on 2017. 4. 8..
 */

public class CommentWriteModel {
    @SerializedName("id") public int mCommentId;
    @SerializedName("user") public User mUserInfo;
    @SerializedName("content") public String mContent;
    @SerializedName("parent_id") public int mParentCommentId;
    @SerializedName("timestamp") public String mTimestamp;
}
