package com.mydegree.renty.service;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.dao.entity.UserEntity;

import java.util.HashSet;
import java.util.Set;

public class AbstractTestFactory {
    protected UserEntity createMockUser() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("alex");
        userEntity.setPassword("password");
        userEntity.setAuthorities(createMockRoles());
        return userEntity;
    }

    protected Set<RoleEntity> createMockRoles() {
        final Set<RoleEntity> entities = new HashSet<>();
        entities.add(createMockRole("admin"));
        entities.add(createMockRole("owner"));
        return entities;
    }

    protected RoleEntity createMockRole(final String role) {
        final RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole(role);
        return roleEntity;
    }
}
