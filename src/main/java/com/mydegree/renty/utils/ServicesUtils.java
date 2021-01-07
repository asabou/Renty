package com.mydegree.renty.utils;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public class ServicesUtils {
    public static <T> Set<T> convertListToSet(final List<T> list) {
        return Sets.newHashSet(list);
    }
}