package com.property.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date parseDate(String dateStr) throws ParseException {
        return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
    }

    public static Date parseDateTime(String dateTimeStr) throws ParseException {
        return new SimpleDateFormat(DATETIME_FORMAT).parse(dateTimeStr);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static String formatDateTime(Date date) {
        return new SimpleDateFormat(DATETIME_FORMAT).format(date);
    }

    public static boolean isDateValid(String dateStr) {
        try {
            parseDate(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}