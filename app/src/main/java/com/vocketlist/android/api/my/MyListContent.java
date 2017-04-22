package com.vocketlist.android.api.my;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SeungTaek.Lim on 2017. 4. 22..
 */

public final class MyListContent {
    @SerializedName("id") public int mId;
    @SerializedName("content") public String mContent;
    @SerializedName("is_done") public boolean mIsDone;
}
