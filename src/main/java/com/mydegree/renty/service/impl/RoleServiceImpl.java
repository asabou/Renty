package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.dao.repository.IRoleRepository;
import com.mydegree.renty.service.abstracts.IRoleService;
import com.mydegree.renty.service.helper.RoleTransformer;
import com.mydegree.renty.service.model.RoleDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;

    public RoleServiceImpl(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDTO> findAllRoles() {
        final Iterable<RoleEntity> entities = roleRepository.findAll();
        return RoleTransformer.transformRoleEntities(entities);
    }
}
