package com.vocketlist.android.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kinamare on 2017-02-24.
 */

public class MyList implements Serializable {
    public List<Data> data;
    public static class Data implements Serializable {
        public int id;
    }
}
