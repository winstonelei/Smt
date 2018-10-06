package com.github.ftpDownloader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 */
public class TimeCommonUtils {
    public static String getNowDateStr() {
        Date now = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        return date_format.format(now);
    }

    public static String yesterdayStr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date calTime = cal.getTime();
        return dateFormat.format(calTime);
    }

    public static List<String> getDateDayListStartAndEnd(String startDay, String endDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int dateField = Calendar.DAY_OF_MONTH;
        List<String> dateList = new ArrayList<>();

        try {
            Date beginDay = dateFormat.parse(startDay);
            Date endDate = dateFormat.parse(endDay);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beginDay);
            while (beginDay.compareTo(endDate) <= 0) {
                dateList.add(dateFormat.format(beginDay));
                calendar.add(dateField, 1);
                beginDay = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateList;
    }
}
