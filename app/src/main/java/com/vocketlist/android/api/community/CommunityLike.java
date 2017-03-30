package com.vocketlist.android.api.community;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SeungTaek.Lim on 2017. 3. 30..
 */

public class CommunityLike {
    @SerializedName("post") public String mPost;
    @SerializedName("is_like") public boolean mIsLike;
    @SerializedName("user") public int mUser;
}
