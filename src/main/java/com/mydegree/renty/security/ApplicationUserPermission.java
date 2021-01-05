package com.mydegree.renty.security;

import lombok.Getter;

@Getter
public enum ApplicationUserPermission {
    OWNER_READ("place:read"),
    OWNER_WRITE("place:write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }
}
