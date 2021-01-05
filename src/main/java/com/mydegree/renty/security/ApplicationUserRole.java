package com.mydegree.renty.security;

import com.google.common.collect.Sets;
import com.mydegree.renty.service.model.Role;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ApplicationUserRole {
    PLACE_OWNER(Sets.newHashSet(ApplicationUserPermission.OWNER_READ,
                                        ApplicationUserPermission.OWNER_WRITE)),
    ADMIN(Sets.newHashSet(ApplicationUserPermission.ADMIN_READ,
            ApplicationUserPermission.ADMIN_WRITE,
            ApplicationUserPermission.OWNER_WRITE,
            ApplicationUserPermission.OWNER_READ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<Role> getGrantedAuthorities() {
        Set<Role> permissions = getPermissions().stream()
                .map(permission -> new Role(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new Role("ROLE_" + this.name()));
        return permissions;
    }
}
