package com.mydegree.renty.service.impl.factories;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.dao.entity.UserEntity;
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
        return Arrays.asList(createMockRole("admin"), createMockRole("owner"), createMockRole("renter"));
    }

    public List<RoleEntity> createMockOwnerRoles() {
        return Arrays.asList(createMockRole("owner"), createMockRole("renter"));
    }

    public List<RoleEntity> createMockRenterRoles() {
        return Arrays.asList(createMockRole("renter"));
    }

    public UserDetailsDTO createMockOwnerUserDetailsDTO(final String username) {
        final UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUser(UserTransformer.transformUserEntity(createMockUserOwner(username)));
        userDetailsDTO.setFirstName(username);
        userDetailsDTO.setLastName(username);
        userDetailsDTO.setEmail(username + "@gmail.com");
        userDetailsDTO.setTelNumber("0758866766");
        return userDetailsDTO;
    }
}
