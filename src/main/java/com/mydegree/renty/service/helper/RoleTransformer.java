package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.service.model.Role;

import java.util.HashSet;
import java.util.Set;

public class RoleTransformer {
    public static Role transform(final RoleEntity input) {
        final Role target = new Role();
        target.setId(input.getId());
        target.setRole(input.getRole());
        return target;
    }

    public static RoleEntity transform(final Role input) {
        final RoleEntity target = new RoleEntity();
        target.setId(input.getId());
        target.setRole(input.getRole());
        return target;
    }

    public static Set<RoleEntity> transform(final Set<Role> roles) {
        final Set<RoleEntity> entities = new HashSet<>();
        roles.forEach((role) -> {
            entities.add(transform(role));
        });
        return entities;
    }

    public static Set<Role> transformToDTO(final Set<RoleEntity> entities) {
        final Set<Role> roles = new HashSet<>();
        entities.forEach((entity) -> {
            roles.add(transform(entity));
        });
        return roles;
    }
}
