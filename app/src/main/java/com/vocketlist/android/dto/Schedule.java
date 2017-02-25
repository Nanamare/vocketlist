package com.vocketlist.android.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SeungTaek.Lim on 2017. 2. 25..
 */

public class Schedule implements Serializable {
    public ScheduleType mType;

    public String mHeaderTitle;

    public String mDay;
    public Date mStartDate;
    public Date mEndDate;
    public String mTitle;

    public enum ScheduleType {
        GROUP_HEADER (1),
        GROUP_ITEM (2);

        private final int mValue;

        private ScheduleType(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }

        public static ScheduleType valueOf(int value) {
            for (ScheduleType type : values()) {
                if (type.mValue == value) {
                    return type;
                }
            }

            return null;
        }
    }
}
