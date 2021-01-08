package com.mydegree.renty.utils;

public class Base64Utils {
    private static java.util.Base64.Decoder getDecoder() {
        return java.util.Base64.getDecoder();
    }

    private static java.util.Base64.Encoder getEncoder() {
        return java.util.Base64.getEncoder();
    }

    public static String decode(String string) {
        final byte[] bytes = getDecoder().decode(string);
        return new String(bytes);
    }

    public static String encode(String string) {
        return getEncoder().encodeToString(string.getBytes());
    }

    public static String getPassword(String string) { return string.split(":")[1]; }

    public static String getUsername(String string) {
        return string.split(":")[0];
    }
}
