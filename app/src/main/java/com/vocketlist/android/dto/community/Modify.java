package com.vocketlist.android.dto.community;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SeungTaek.Lim on 2017. 2. 26..
 */
public class Modify {
    @SerializedName("id") public int mId;
    @SerializedName("image") public String mImageUrl;
    @SerializedName("content") public String mContent;
    @SerializedName("service_id") public int mServiceId;
}
