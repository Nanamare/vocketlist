package com.vocketlist.android.api.community.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Author implements Serializable {
    @SerializedName("id") public int mId;
    @SerializedName("email") public String mEmail;
    @SerializedName("name") public String mName;
    @SerializedName("image") public String mImageUrl;
}