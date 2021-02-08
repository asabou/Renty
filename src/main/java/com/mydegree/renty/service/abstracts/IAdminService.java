package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.RoleDTO;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;

import java.util.List;
import java.util.Set;

public interface IAdminService {
    void updateRolesForUser(final UserDetailsDTO userDetails);
}
