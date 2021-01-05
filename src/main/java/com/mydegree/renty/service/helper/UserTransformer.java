package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.service.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserTransformer {
    public static User transform(final UserEntity input) {
        final User target = new User();
        target.setId(input.getId());
        target.setUsername(input.getUsername());
        //TODO passwords are not supposed to be send in json
        //target.setPassword(input.getPassword());
        target.setRoles(RoleTransformer.transformToDTO(input.getAuthorities()));
        return target;
    }

    public static UserEntity transform(final User input) {
        final UserEntity target = new UserEntity();
        target.setId(input.getId());
        target.setUsername(input.getUsername());
        target.setPassword(input.getPassword());
        target.setAuthorities(RoleTransformer.transform(input.getRoles()));
        return target;
    }

    public static List<UserEntity> transform(final List<User> users) {
        final List<UserEntity> entities = new ArrayList<>();
        users.forEach((user) -> {
            entities.add(transform(user));
        });
        return entities;
    }

    public static List<User> transform(final Iterable<UserEntity> entities) {
        final List<User> users = new ArrayList<>();
        entities.forEach((entity) -> {
            users.add(transform(entity));
        });
        return users;
    }
}
