package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.IEntertainmentActivityRepository;
import com.mydegree.renty.dao.repository.IRoleRepository;
import com.mydegree.renty.dao.repository.IUserDetailsRepository;
import com.mydegree.renty.dao.repository.IUserRepository;
import com.mydegree.renty.service.abstracts.AbstractService;
import com.mydegree.renty.service.abstracts.IAdminService;
import com.mydegree.renty.service.helper.RoleTransformer;
import com.mydegree.renty.service.model.RoleDTO;
import com.mydegree.renty.utils.ServicesUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminServiceImpl extends AbstractService implements IAdminService {

    public AdminServiceImpl(IUserRepository userRepository, IUserDetailsRepository userDetailsRepository, IRoleRepository roleRepository, IEntertainmentActivityRepository entertainmentActivityRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, userDetailsRepository, roleRepository, entertainmentActivityRepository, passwordEncoder);
    }

    @Override
    public void updateRolesForUsername(String username, List<RoleDTO> roles) {
        final UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throwNotFoundException("User with username: " + username + " not found!");
        }
        final Set<RoleEntity> roleEntities = RoleTransformer.transformRoles(ServicesUtils.convertListToSet(roles));
        user.setAuthorities(roleEntities);
        userRepository.save(user);
    }

    @Override
    public void updateRolesForUserId(Long id, List<RoleDTO> roles) {
        final Optional<UserEntity> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throwNotFoundException("User with username: " + id + " not found!");
        }
        final UserEntity user = userOptional.get();
        final Set<RoleEntity> roleEntities = RoleTransformer.transformRoles(ServicesUtils.convertListToSet(roles));
        user.setAuthorities(roleEntities);
        userRepository.save(user);
    }
}
