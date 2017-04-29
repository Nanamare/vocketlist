package com.vocketlist.android.api.address;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SeungTaek.Lim on 2017. 4. 29..
 */

public final class AddressFirstInfo {
    @SerializedName("id") int mId;
    @SerializedName("first_register_office") String office;
}
