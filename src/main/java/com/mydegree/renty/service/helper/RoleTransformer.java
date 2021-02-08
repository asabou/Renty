package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.service.model.RoleDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleTransformer {
    private static void fillRole(final RoleEntity input, final RoleDTO target) {
        target.setId(input.getId());
        target.setRole(input.getRole());
        target.setDescription(input.getDescription());
    }

    private static void fillRoleEntity(final RoleDTO input, final RoleEntity target) {
        target.setId(input.getId());
        target.setRole(input.getRole());
        target.setDescription(input.getDescription());
    }

    public static RoleDTO transformRoleEntity(final RoleEntity input) {
        if (input == null) {
            return null;
        }
        final RoleDTO target = new RoleDTO();
        fillRole(input, target);
        return target;
    }

    public static RoleEntity transformRole(final RoleDTO input) {
        if (input == null) {
            return null;
        }
        final RoleEntity target = new RoleEntity();
        fillRoleEntity(input, target);
        return target;
    }

    public static Set<RoleEntity> transformRoles(final Set<RoleDTO> inputs) {
        final Set<RoleEntity> targets = new HashSet<>();
        if (inputs != null) {
            inputs.forEach((role) -> targets.add(transformRole(role)));
        }
        return targets;
    }

    public static Set<RoleDTO> transformRoleEntities(final Set<RoleEntity> inputs) {
        final Set<RoleDTO> targets = new HashSet<>();
        if (inputs != null) {
            inputs.forEach((entity) -> targets.add(transformRoleEntity(entity)));
        }
        return targets;
    }

    public static List<RoleDTO> transformRoleEntities(final Iterable<RoleEntity> inputs) {
        final List<RoleDTO> targets = new ArrayList<>();
        if (inputs != null) {
            inputs.forEach((entity) -> targets.add(transformRoleEntity(entity)));
        }
        return targets;
    }
}
