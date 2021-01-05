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

    public static String code(String string) {
        return getEncoder().encodeToString(string.getBytes());
    }

    public static String []getUsernameAndPasswordDecoded(String string) {
        final String decodedString = decode(string);
        return decodedString.split(":");
    }

    public static String getPasswordDecoded(String string) {
        return getUsernameAndPasswordDecoded(string)[1];
    }

    public static String getUsernameDecoded(String string) {
        return getUsernameAndPasswordDecoded(string)[0];
    }
}
