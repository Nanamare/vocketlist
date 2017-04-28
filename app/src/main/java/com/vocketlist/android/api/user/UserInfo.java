package com.vocketlist.android.api.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SeungTaek.Lim on 2017. 4. 16..
 */
public class UserInfo {
    @SerializedName("id") public int mUserId;
    @SerializedName("name") public String mName;
    @SerializedName("email") public String mEmail;
    @SerializedName("birth") public String mBirth;
    @SerializedName("mylist") public MyListInfo mMyList;
    @SerializedName("gender") public String mGender;
    @SerializedName("image") public String mImageUrl;
}
