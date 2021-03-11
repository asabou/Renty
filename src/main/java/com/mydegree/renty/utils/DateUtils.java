package com.mydegree.renty.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

public class DateUtils {
    public static Timestamp getCurrentTimestamp() { return Timestamp.from(Instant.now()); }
    public static Date getCurrentDate() { return new Date(System.currentTimeMillis()); }

    public static SimpleDateFormat formatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parseStringToSqlDate(final String string) {
        try {
            return new Date(formatyyyyMMdd.parse(string).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(System.currentTimeMillis());
    }

    public static Date getDateFrom1970() {
        return new Date(0);
    }

    public static Date getDateFrom3000() {
        return new Date(32503680000000L);
    }

    public static Date getMinTemporalDateFromString(final String date) {
        return date == null ? getDateFrom1970() : parseStringToSqlDate(date);
    }

    public static Date getMaxTemporalDateFromString(final String date) {
        return date == null ? getDateFrom3000() : parseStringToSqlDate(date);
    }
}
