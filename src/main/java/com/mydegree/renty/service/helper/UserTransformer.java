package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.service.model.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserTransformer {
    private static void fillUser(final UserEntity input, final UserDTO target) {
        target.setId(input.getId());
        target.setUsername(input.getUsername()); //passwords are not supposed to be sent to client
        target.setRoles(RoleTransformer.transformRolesEntities(input.getAuthorities()));
    }

    private static void fillUserEntity(final UserDTO input, final UserEntity target) {
        target.setId(input.getId());
        target.setUsername(input.getUsername());
        target.setPassword(input.getPassword()); //password when a user is created
        target.setAuthorities(RoleTransformer.transformRoles(input.getRoles()));
    }

    public static UserDTO transform(final UserEntity input) {
        if (input == null) {
            return null;
        }
        final UserDTO target = new UserDTO();
        fillUser(input, target);
        return target;
    }

    public static UserEntity transform(final UserDTO input) {
        final UserEntity target = new UserEntity();
        target.setId(input.getId());
        target.setUsername(input.getUsername());
        target.setPassword(input.getPassword());
        target.setAuthorities(RoleTransformer.transformRoles(input.getRoles()));
        return target;
    }

    public static List<UserEntity> transform(final List<UserDTO> inputs) {
        final List<UserEntity> targets = new ArrayList<>();
        inputs.forEach((user) -> targets.add(transform(user)));
        return targets;
    }

    public static List<UserDTO> transform(final Iterable<UserEntity> entities) {
        final List<UserDTO> users = new ArrayList<>();
        entities.forEach((entity) -> users.add(transform(entity)));
        return users;
    }
}
