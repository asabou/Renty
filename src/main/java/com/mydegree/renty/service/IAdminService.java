package com.mydegree.renty.service;

import com.mydegree.renty.service.model.Role;
import com.mydegree.renty.service.model.User;

import java.util.List;
import java.util.Set;

public interface IAdminService {
    void saveUser(final User user);
    void deleteUserByUserName(final String username);
    void deleteUserByUserId(final Long id);
    List<User> findAllUsers();
    void updateRolesForUser(final String username, final Set<Role> roles);
}
