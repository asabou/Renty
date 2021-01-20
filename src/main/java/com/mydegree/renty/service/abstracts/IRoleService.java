package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.RoleDTO;

import java.util.List;

public interface IRoleService {
    List<RoleDTO> findAllRoles();
}
