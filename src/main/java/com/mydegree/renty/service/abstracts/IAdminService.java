package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.RoleDTO;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;

import java.util.List;
import java.util.Set;

public interface IAdminService {
    void updateRolesForUsername(final String username, final List<RoleDTO> roles);
    void updateRolesForUserId(final Long id, final List<RoleDTO> roles);
}
