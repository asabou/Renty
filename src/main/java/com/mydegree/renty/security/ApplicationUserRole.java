package com.mydegree.renty.security;

import com.google.common.collect.Sets;
import com.mydegree.renty.service.model.RoleDTO;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum ApplicationUserRole {
    RENTER(Sets.newHashSet(ApplicationUserPermission.RENTER_READ,
            ApplicationUserPermission.RENTER_WRITE)),
    PLACE_OWNER(Sets.newHashSet(ApplicationUserPermission.OWNER_READ,
            ApplicationUserPermission.OWNER_WRITE,
            ApplicationUserPermission.RENTER_READ,
            ApplicationUserPermission.RENTER_WRITE)),
    ADMIN(Sets.newHashSet(ApplicationUserPermission.ADMIN_READ,
            ApplicationUserPermission.ADMIN_WRITE,
            ApplicationUserPermission.OWNER_WRITE,
            ApplicationUserPermission.OWNER_READ,
            ApplicationUserPermission.RENTER_READ,
            ApplicationUserPermission.RENTER_WRITE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<RoleDTO> getGrantedAuthorities() {
        Set<RoleDTO> permissions = getPermissions().stream()
                .map(permission -> new RoleDTO(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new RoleDTO("ROLE_" + this.name()));
        return permissions;
    }
}
