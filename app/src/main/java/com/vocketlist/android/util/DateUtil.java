package com.vocketlist.android.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 유틸 : 시간 관련
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 29.
 */
public class DateUtil {
    public static SimpleDateFormat SERVER_DATETIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static SimpleDateFormat SERVER_TIME = new SimpleDateFormat("HH:mm:ss");

    public static SimpleDateFormat LOCAL_DATETIME = new SimpleDateFormat("yyyy.MM.dd");
    public static SimpleDateFormat LOCAL_TIME = new SimpleDateFormat("HH시 mm분");

    /**
     * 스케줄
     *
     * @param datetime
     * @return
     */
    public static String convertDateForSchedule(String datetime) {
        try {
            Date date = SERVER_DATETIME.parse(datetime);
            return LOCAL_DATETIME.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return datetime;
    }

    /**
     * 스케줄
     *
     * @param time
     * @return
     */
    public static String convertTimeForSchedule(String time) {
        try {
            Date date = SERVER_TIME.parse(time);
            return LOCAL_TIME.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }
}
