package com.vocketlist.android.api.community.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */
public class CommunityWrite {
    @SerializedName("id") public int mCommunityId;
    @SerializedName("image") public String mImagePath;
    @SerializedName("content") public String mContent;
    @SerializedName("service_id") public int mVocketServiceId;
}
