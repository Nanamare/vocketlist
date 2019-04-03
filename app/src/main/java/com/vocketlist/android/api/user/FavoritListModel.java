package com.vocketlist.android.api.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 4. 22..
 */

public class FavoritListModel {
    @SerializedName("favorite") public List<String> mFavoriteList;
    @SerializedName("region") public List<Region> mAddress;

    public static final class Region {
        @SerializedName("first_register_office") public int mFirstAddressId;
        @SerializedName("second_register_office") public int mSecondAddressId;
    }
}
