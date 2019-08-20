package com.yalantis.ucrop.util;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String MONTH_DAY_FORMAT = "MM-dd";
    public static String MINUTE_SECOND_FORMAT = "HH:mm";

    public static String getFormatString(Calendar calendar) {
        return getFormatString(calendar, DEFAULT_DATE_FORMAT);
    }

    public static Calendar timemillisToCalendar(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return calendar;
    }

    public static Calendar stringToCalendar(String time) {
        Date date;
        Calendar calendar = Calendar.getInstance();
        if (time.length() < DATE_FORMAT.length()) {
            time = time + ":00";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            date = sdf.parse(time);
            calendar.setTime(date);
        } catch (ParseException e) {

        }
        return calendar;
    }


    public static String getFormatString(Calendar calendar, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Date date = calendar.getTime();
        return sdf.format(date);
    }

    public static String getWeekString(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        return getWeekString(calendar);
    }

    /**
     * 根据一些规则显示日期
     *
     * @return
     */
    public static String getShowString(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        return getShowString(calendar);
    }

    /**
     * 根据一些规则显示日期,用于任务
     *
     * @param calendar
     * @return
     */
    public static String getShowString(Calendar calendar) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        int daysBetween = daysBetween(today, calendar);
        switch (daysBetween) {
            case 0:
                return "今天";
            case 1:
                return "昨天";
            case -1:
                return "明天";
            case -2:
                return "后天";
        }
        if (isSameWeek(today, calendar)) {
            return getWeekString(calendar);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(MONTH_DAY_FORMAT, Locale.CHINA);
        Date date = calendar.getTime();
        return sdf.format(date);
    }

    /**
     * 根据一些规则显示日期,用于消息
     *
     * @param calendar
     * @return
     */
    public static String getMsgShowString(Calendar calendar) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(calendar.getTimeInMillis());
        String showString = getShowString(calendar);
        if ("今天".equals(showString)) {
            SimpleDateFormat sdf = new SimpleDateFormat(MINUTE_SECOND_FORMAT, Locale.CHINA);
            Date date = c.getTime();
            return sdf.format(date);
        } else {
            return showString;
        }

    }

    public static boolean isSameWeek(Calendar c1, Calendar c2) {
        int i = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        switch (i) {
            case 0:
                return c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR);
            case 1:
                return c1.get(Calendar.WEEK_OF_YEAR) == 1 && c2.get(Calendar.WEEK_OF_YEAR) == 52 && c1.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
            case -1:
                return c1.get(Calendar.WEEK_OF_YEAR) == 52 && c2.get(Calendar.WEEK_OF_YEAR) == 1 && c2.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
        }
        return false;
    }

    public static int getShowColor(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        return getShowColor(calendar);
    }

    public static @ColorInt
    int getShowColor(Calendar calendar) {
        int days = daysBetween(calendar, Calendar.getInstance());
        if (days >= 0) {
            return Color.parseColor("#37c597");
        } else if (days > -7) {
            return Color.RED;
        }
        return Color.BLACK;
    }

    /**
     * @param c1
     * @param c2
     * @return 返回c1比c2 多的日期差
     */
    public static int daysBetween(Calendar c1, Calendar c2) {
//        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
//            return c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR);
//        }

        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        c2.set(Calendar.HOUR_OF_DAY, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);

        long time1 = c1.getTimeInMillis();
        long time2 = c2.getTimeInMillis();
        long between_days = (time1 - time2) / (1000 * 3600 * 24);
        return (int) between_days;
    }

    public static String getWeekString(Calendar calendar) {
        int d = calendar.get(Calendar.DAY_OF_WEEK);
        switch (d) {
            case Calendar.SUNDAY:
                return "星期日";
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
        }
        return "";
    }

    public static String format(Date date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取当前时间 如：2018-01-05 12:22:01
     */
    public static String getCurrentTime() {
        //获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        return time;
    }
}
