package com.mydegree.renty.service;

import com.mydegree.renty.service.model.RoleDTO;
import com.mydegree.renty.service.model.UserDTO;

import java.util.List;
import java.util.Set;

public interface IAdminService {
    void saveUser(final UserDTO user);
    void deleteUserByUserName(final String username);
    void deleteUserByUserId(final Long id);
    List<UserDTO> findAllUsers();
    void updateRolesForUser(final String username, final Set<RoleDTO> roles);
}
