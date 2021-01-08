package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.service.model.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserTransformer {
    private static void fillUser(final UserEntity input, final UserDTO target) {
        target.setId(input.getId());
        target.setUsername(input.getUsername()); //passwords are not supposed to be sent to client
        target.setRoles(RoleTransformer.transformRoleEntities(input.getAuthorities()));
    }

    private static void fillUserEntity(final UserDTO input, final UserEntity target) {
        target.setId(input.getId());
        target.setUsername(input.getUsername());
        target.setPassword(input.getPassword()); //password when a user is created
        target.setAuthorities(RoleTransformer.transformRoles(input.getRoles()));
    }

    public static UserDTO transformUserEntity(final UserEntity input) {
        if (input == null) {
            return null;
        }
        final UserDTO target = new UserDTO();
        fillUser(input, target);
        return target;
    }

    public static UserEntity transformUser(final UserDTO input) {
        if (input == null) {
            return null;
        }
        final UserEntity target = new UserEntity();
        fillUserEntity(input, target);
        return target;
    }

    public static List<UserEntity> transformUsers(final List<UserDTO> inputs) {
        final List<UserEntity> targets = new ArrayList<>();
        inputs.forEach((user) -> targets.add(transformUser(user)));
        return targets;
    }

    public static List<UserDTO> transformUserEntities(final Iterable<UserEntity> entities) {
        final List<UserDTO> users = new ArrayList<>();
        entities.forEach((entity) -> users.add(transformUserEntity(entity)));
        return users;
    }
}
