package com.mydegree.renty.utils;

import com.google.common.collect.Sets;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Set;

public class ServicesUtils {
    public static <T> Set<T> convertListToSet(final List<T> list) {
        return Sets.newHashSet(list);
    }

    public static String transformToJSONError(final String message) {
        return (new JSONObject().put("message", message)).toString();
    }

    public static String wildCardParam(final String string) {
        if (string == null) {
            return "%";
        }
        return "%" + string.replaceAll("[ _?,:']", "%")
                .replaceAll("[A-Z]+", "%") + "%";
    }

    public static boolean isStringNullOrEmpty(final String string) {
        return string == null || string.isEmpty();
    }

    public static Long convertStringToLong(final String string) {
        return Long.parseLong(string);
    }

    public static Double convertStringToDouble(final String string) {
        return Double.parseDouble(string);
    }
}