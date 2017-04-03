package com.vocketlist.android.dto;

import com.google.gson.annotations.SerializedName;

public class Link {
	@SerializedName("next") public int mNext;
	@SerializedName("previous") public int mPrevious;
}