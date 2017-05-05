package com.vocketlist.android.api.address;

import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 4. 29..
 */

public class FirstAddress {
    public final int mId;
    public final String mName;

    public FirstAddress(int id, String addressName) {
        mId = id;
        mName = addressName;
    }

    public String getAddress(){
        return mName;
    }

}
