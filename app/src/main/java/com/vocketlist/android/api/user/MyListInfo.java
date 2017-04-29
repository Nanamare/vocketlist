package com.vocketlist.android.api.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SeungTaek.Lim on 2017. 4. 16..
 */

public final class MyListInfo {
    @SerializedName("total_mylist") public int mTotal;
    @SerializedName("finfinish_mylistish") public int mFinish;
    @SerializedName("mylist_completion_rate") public float mRemain;
}
