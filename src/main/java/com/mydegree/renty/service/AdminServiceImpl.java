package com.mydegree.renty.service;

import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.IUserRepository;
import com.mydegree.renty.exceptions.BadRequestException;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.helper.RoleTransformer;
import com.mydegree.renty.service.helper.UserTransformer;
import com.mydegree.renty.service.model.RoleDTO;
import com.mydegree.renty.service.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminServiceImpl implements IAdminService {
    private final IUserRepository userRepository;

    public AdminServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(UserDTO user) {
        final UserEntity userEntity = userRepository.findUserByUsername(user.getUsername());
        if (userEntity != null) {
            throw new BadRequestException("This user already exists!");
        }
        userRepository.save(UserTransformer.transform(user));
    }

    @Override
    public void deleteUserByUserName(String username) {
        final UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new NotFoundException("Cannot find user with username: " + username);
        }
        userRepository.deleteById(user.getId());
    }

    @Override
    public void deleteUserByUserId(Long id) {
        final Optional<UserEntity> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Cannot find user with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        Iterable<UserEntity> all = userRepository.findAll();
        return UserTransformer.transform(all);
    }

    @Override
    public void updateRolesForUser(String username, Set<RoleDTO> roles) {
        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new NotFoundException("Cannot find user with username: " + username);
        }
        user.setAuthorities(RoleTransformer.transformRoles(roles));
        userRepository.save(user);
    }
}
