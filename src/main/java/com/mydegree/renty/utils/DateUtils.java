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
}
