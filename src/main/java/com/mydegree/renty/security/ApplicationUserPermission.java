package com.mydegree.renty.security;

import lombok.Getter;

@Getter
public enum ApplicationUserPermission {
    OWNER_READ("place:read"),
    OWNER_WRITE("place:write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    RENTER_READ("renter:read"),
    RENTER_WRITE("renter:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }
}
