package com.mydegree.renty.service.impl.factories;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.service.helper.UserDetailsTransformer;
import com.mydegree.renty.service.helper.UserTransformer;
import com.mydegree.renty.service.model.UserDetailsDTO;
import com.mydegree.renty.utils.ServicesUtils;

import java.util.Arrays;
import java.util.List;

public class UserFactory {
    private UserEntity createSimpleMockUser(final String username) {
        final UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("password");
        return user;
    }

    public UserEntity createMockUserAdmin(final String username) {
        final UserEntity user = createSimpleMockUser(username);
        user.setAuthorities(ServicesUtils.convertListToSet(createMockAdminRoles()));
        return user;
    }

    public UserEntity createMockUserOwner(final String username) {
        final UserEntity user = createSimpleMockUser(username);
        user.setAuthorities(ServicesUtils.convertListToSet(createMockOwnerRoles()));
        return user;
    }

    public UserEntity createMockUserRenter(final String username) {
        final UserEntity user = createSimpleMockUser(username);
        user.setAuthorities(ServicesUtils.convertListToSet(createMockRenterRoles()));
        return user;
    }

    public RoleEntity createMockRole(final String role) {
        final RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole(role);
        return roleEntity;
    }

    public List<RoleEntity> createMockAdminRoles() {
        return Arrays.asList(createMockRole("ADMIN"), createMockRole("OWNER"), createMockRole("renter"));
    }

    public List<RoleEntity> createMockOwnerRoles() {
        return Arrays.asList(createMockRole("OWNER"), createMockRole("RENTER"));
    }

    public List<RoleEntity> createMockRenterRoles() {
        return Arrays.asList(createMockRole("RENTER"));
    }

    public UserDetailsDTO createMockOwnerUserDetailsDTO(final String username) {
        final UserDetailsDTO userDetailsDTO = UserDetailsTransformer.transformUserDetailsEntity(createSimpleMockUserDetailsEntity(username));
        userDetailsDTO.setUser(UserTransformer.transformUserEntity(createMockUserOwner(username)));
        return userDetailsDTO;
    }

    public UserDetailsEntity createSimpleMockUserDetailsEntity(final String name) {
        final UserDetailsEntity entity = new UserDetailsEntity();
        entity.setFirstName(name);
        entity.setLastName(name);
        entity.setEmail(name + "@gmail.com");
        entity.setTelNumber("0758866769");
        return entity;
    }

    public List<UserEntity> createSimpleMockUserEntities() {
        return Arrays.asList(createSimpleMockUser("alex"));
    }

    public List<UserDetailsEntity> createSimpleMockUserDetailsEntities() {
        return Arrays.asList(createSimpleMockUserDetailsEntity("alex"));
    }
}
