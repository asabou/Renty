package com.mydegree.renty.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TokenUtils {
    public static Claims getClaimsFromTokenUsingSecretKey(final String token, final SecretKey secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public static Long getUserIdFromTokenUsingSecretKey(final String token, final SecretKey secretKey) {
        final Claims claims = getClaimsFromTokenUsingSecretKey(token, secretKey);
        return ServicesUtils.convertStringToLong(claims.get(Constants.userId).toString());
    }

    public static String getUsernameFromTokenUsingSecretKey(final String token, final SecretKey secretKey) {
        final Claims claims = getClaimsFromTokenUsingSecretKey(token, secretKey);
        return claims.get(Constants.sub).toString();
    }

    public static Collection<SimpleGrantedAuthority> getAuthoritiesFromTokenUsingSecretKey(final String token, final SecretKey secretKey) {
        final Claims claims = getClaimsFromTokenUsingSecretKey(token, secretKey);
        List<Map<String, String>> authorities = (List<Map<String, String>>) claims.get(Constants.authorities);
        Set<SimpleGrantedAuthority> authority = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                .collect(Collectors.toSet());
        return authority;
    }

    private static boolean isRoleInAuthorities(final String role, final String token, final SecretKey secretKey) {
        final Collection<SimpleGrantedAuthority> authorities = getAuthoritiesFromTokenUsingSecretKey(token, secretKey);
        for (SimpleGrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAdminRole(final String token, final SecretKey secretKey) {
        return isRoleInAuthorities("ADMIN", token, secretKey);
    }

    public static boolean hasOwnerRole(final String token, final SecretKey secretKey) {
        return isRoleInAuthorities("OWNER", token, secretKey);
    }

    public static String getTokenFromAuthorizationHeader(final String authorization) {
        return authorization.split(" ")[1];
    }


}
