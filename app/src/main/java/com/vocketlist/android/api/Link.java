package com.vocketlist.android.api;

import com.google.gson.annotations.SerializedName;

public class Link {
    @SerializedName("next") public int mNextId;
    @SerializedName("previous") public int mPreviousId;
}