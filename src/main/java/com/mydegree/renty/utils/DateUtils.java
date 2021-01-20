package com.mydegree.renty.utils;

import java.sql.Timestamp;
import java.time.Instant;

public class DateUtils {
    public static Timestamp getCurrentTimestamp() { return Timestamp.from(Instant.now()); }
}
