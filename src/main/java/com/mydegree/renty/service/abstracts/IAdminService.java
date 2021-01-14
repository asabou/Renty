package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import com.mydegree.renty.service.model.RoleDTO;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;

import java.util.List;
import java.util.Set;

public interface IAdminService {
    void saveUserOwner(final UserDetailsDTO userDetails);
    void saveUserAdmin(final UserDTO user);
    List<UserDTO> findAllUsers();
    void updateRolesForUser(final String username, final Set<RoleDTO> roles);
    void saveEntertainmentActivity(final EntertainmentActivityDTO entertainmentActivity);
}
