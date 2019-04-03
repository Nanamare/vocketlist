package com.vocketlist.android.api.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */

public class LoginModel {
    @SerializedName("token") public String mToken;
    @SerializedName("user") public UserInfo mUserInfo;
    @SerializedName("interest") public List<String> mInterest;
    @SerializedName("address") public List<Address> mAddress;
}
