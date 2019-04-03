package com.vocketlist.android.api.my;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 4. 22..
 */

public final class MyListModel implements Serializable {
    @SerializedName("data") public List<MyList> mMyListList;



    public static class MyList implements Serializable {
        @SerializedName("id") public int mId;
        @SerializedName("content") public String mContent;
        @SerializedName("is_done") public boolean mIsDone;
    }
}
