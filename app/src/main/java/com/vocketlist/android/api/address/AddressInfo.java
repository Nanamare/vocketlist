package com.vocketlist.android.api.address;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 4. 29..
 */

public final class AddressInfo {
    @SerializedName("id") public int mId;
    @SerializedName("first_register_office") public String mAddressName;
    @SerializedName("second_register_office") public List<SecondAddress> mSecondAddress;

    public static final class SecondAddress {
        @SerializedName("id") public int mId;
        @SerializedName("second_register_office") public String mAddressName;
    }
}
