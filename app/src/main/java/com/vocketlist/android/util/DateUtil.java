package com.vocketlist.android.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 유틸 : 시간 관련
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 29.
 */
public class DateUtil {
    public static SimpleDateFormat SERVER_DATETIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat SERVER_TIME = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public static SimpleDateFormat LOCAL_DATETIME = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
    public static SimpleDateFormat LOCAL_TIME = new SimpleDateFormat("HH시 mm분", Locale.getDefault());

    public static SimpleDateFormat COMMENT_READ = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

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

    private static final int SEC = 60;
    private static final int MIN = 60;
    private static final int HOUR = 24;
    private static final int DAY = 7;
    private static final int MONTH = 12;

    /**
     * 변환 : 댓글
     *
     * @param dateString
     * @return
     */
    public static String convertForComment(String dateString) {
        try {
            Date date = SERVER_DATETIME.parse(dateString);

            Calendar cur = Calendar.getInstance();
            Calendar reg = (Calendar) cur.clone();
            reg.setTime(date);

            String msg = "";
            long diff = (cur.getTimeInMillis() - reg.getTimeInMillis()) / 1000;

            if(diff < SEC) msg = "방금 전";
            else if ((diff /= SEC) < MIN) msg = diff + "분 전";
            else if ((diff /= MIN) < HOUR) msg = diff + "시간 전";
            else if ((diff /= HOUR) < DAY) msg = diff + "일 전";
            else msg = COMMENT_READ.format(date);

            return msg;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }
}
