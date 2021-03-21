package com.mydegree.renty.utils;

import javax.crypto.SecretKey;

public class RestUtils {
    public static String getUsernameFromAuthHeaderUsingSecretKey(final String authorization, final SecretKey secretKey) {
        final String token = TokenUtils.getTokenFromAuthorizationHeader(authorization);
        return TokenUtils.getUsernameFromTokenUsingSecretKey(token, secretKey);
    }

    public static Long getUserDetailsIdFromAuthHeaderUsingSecretKey(final String authorization, final SecretKey secretKey) {
        final String token = TokenUtils.getTokenFromAuthorizationHeader(authorization);
        return TokenUtils.getUserIdFromTokenUsingSecretKey(token, secretKey);
    }
}
