package com.vocketlist.android.api.community.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SeungTaek.Lim on 2017. 3. 30..
 */

public class CommunityLike {
    @SerializedName("post_id") public int mPost;
    @SerializedName("is_like") public boolean mIsLike;
    @SerializedName("user_id") public int mUser;
}
