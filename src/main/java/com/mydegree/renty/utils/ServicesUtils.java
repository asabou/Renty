package com.mydegree.renty.utils;

import com.google.common.collect.Sets;
import org.json.JSONObject;

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
        return string.replaceAll("[ _?,:']", "%")
                .replaceAll("[A-Z]+", "%")
                .replaceAll("[0-9]+", "%");
    }

}