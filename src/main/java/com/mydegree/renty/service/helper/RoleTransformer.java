package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.service.model.RoleDTO;

import java.util.HashSet;
import java.util.Set;

public class RoleTransformer {
    private static void fillRole(final RoleEntity input, final RoleDTO target) {
        target.setId(input.getId());
        target.setRole(input.getRole());
    }

    private static void fillRoleEntity(final RoleDTO input, final RoleEntity target) {
        target.setId(input.getId());
        target.setRole(input.getRole());
    }

    public static RoleDTO transformRoles(final RoleEntity input) {
        if (input == null) {
            return null;
        }
        final RoleDTO target = new RoleDTO();
        fillRole(input, target);
        return target;
    }

    public static RoleEntity transformRoles(final RoleDTO input) {
        if (input == null) {
            return null;
        }
        final RoleEntity target = new RoleEntity();
        fillRoleEntity(input, target);
        return target;
    }

    public static Set<RoleEntity> transformRoles(final Set<RoleDTO> inputs) {
        final Set<RoleEntity> targets = new HashSet<>();
        inputs.forEach((role) -> targets.add(transformRoles(role)));
        return targets;
    }

    public static Set<RoleDTO> transformRolesEntities(final Set<RoleEntity> inputs) {
        final Set<RoleDTO> targets = new HashSet<>();
        inputs.forEach((entity) -> targets.add(transformRoles(entity)));
        return targets;
    }
}
