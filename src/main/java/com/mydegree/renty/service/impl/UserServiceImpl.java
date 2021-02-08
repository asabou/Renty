package com.mydegree.renty.service.impl;

import com.google.common.collect.Sets;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.service.abstracts.AbstractService;
import com.mydegree.renty.service.abstracts.IUserService;
import com.mydegree.renty.service.helper.RoleTransformer;
import com.mydegree.renty.service.helper.UserDetailsTransformer;
import com.mydegree.renty.service.helper.UserTransformer;
import com.mydegree.renty.service.model.RoleDTO;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;
import com.mydegree.renty.utils.ServicesUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends AbstractService implements IUserService {
    private final SecretKey secretKey;
    public UserServiceImpl(IUserRepository userRepository,
                           IUserDetailsRepository userDetailsRepository,
                           IRoleRepository roleRepository,
                           IEntertainmentActivityRepository entertainmentActivityRepository,
                           PasswordEncoder passwordEncoder, SecretKey secretKey,
                           IReservationRepository reservationRepository) {
        super(userRepository, userDetailsRepository, roleRepository, entertainmentActivityRepository, passwordEncoder, reservationRepository);
        this.secretKey = secretKey;
    }

    @Override
    public void updateAccount(UserDetailsDTO userDetails) {
        updateUser(userDetails);
    }

    @Override
    public void createAdminUser(UserDetailsDTO userDetails) {
        final RoleDTO adminRole = RoleTransformer.transformRoleEntity(roleRepository.findRoleEntityByRole("ADMIN"));
        final RoleDTO ownerRole = RoleTransformer.transformRoleEntity(roleRepository.findRoleEntityByRole("OWNER"));
        final RoleDTO renterRole = RoleTransformer.transformRoleEntity(roleRepository.findRoleEntityByRole("RENTER"));
        if (adminRole == null || ownerRole == null || renterRole == null) {
            throwInternalServerErrorException("Authorities for admin not found!");
        }
        userDetails.getUser().setRoles(Sets.newHashSet(adminRole, ownerRole, renterRole));
        createUser(userDetails);
    }

    @Override
    public void createOwnerUser(UserDetailsDTO userDetails) {
        final RoleDTO ownerRole = RoleTransformer.transformRoleEntity(roleRepository.findRoleEntityByRole("OWNER"));
        final RoleDTO renterRole = RoleTransformer.transformRoleEntity(roleRepository.findRoleEntityByRole("RENTER"));
        if (ownerRole == null || renterRole == null) {
            throwInternalServerErrorException("Authorities for owner not found!");
        }
        userDetails.getUser().setRoles(Sets.newHashSet(ownerRole, renterRole));
        createUser(userDetails);
    }

    @Override
    public void createRenterUser(UserDetailsDTO userDetails) {
        final RoleDTO renterRole = RoleTransformer.transformRoleEntity(roleRepository.findRoleEntityByRole("RENTER"));
        if (renterRole == null) {
            throwInternalServerErrorException("Authorities for renter not found!");
        }
        userDetails.getUser().setRoles(Sets.newHashSet(renterRole));
        createUser(userDetails);
    }

    @Override
    public void deleteAccountByUserId(Long id) {
        deleteUserById(id);
    }

    @Override
    public void deleteAccountByUsername(String username) {
        deleteUserByUsername(username);
    }

    @Override
    public List<UserDetailsDTO> findAllUserDetails() {
        final Iterable<UserDetailsEntity> entities = userDetailsRepository.findAll();
        return UserDetailsTransformer.transformUsersDetailsEntities(entities);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        final Iterable<UserEntity> entities = userRepository.findAll();
        return UserTransformer.transformUserEntities(entities);
    }

    @Override
    public UserDetailsDTO findUserByUserId(Long id) {
        Optional<UserDetailsEntity> byId = userDetailsRepository.findById(id);
        if (byId.isEmpty()) {
            throwNotFoundException("User with id: " + id + " does not exists!");
        }
        return UserDetailsTransformer.transformUserDetailsEntity(byId.get());
    }

    @Override
    public Long findUserDetailsIdByUsername(String username) {
        UserEntity userByUsername = userRepository.findUserByUsername(username);
        if (userByUsername == null) {
            throwNotFoundException("User with username: " + username + " does not exists!");
        }
        return userByUsername.getId();
    }

    @Override
    public void deleteAccount(String token) {
        final Claims claims = ServicesUtils.getClaimsFromTokenUsingSecretKey(token, secretKey);
        deleteAccountByUsername(claims.get("sub").toString());
    }

}
