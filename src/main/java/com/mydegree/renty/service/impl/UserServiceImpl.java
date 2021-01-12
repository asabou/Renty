package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.IRoleRepository;
import com.mydegree.renty.dao.repository.IUserDetailsRepository;
import com.mydegree.renty.dao.repository.IUserRepository;
import com.mydegree.renty.exceptions.BadRequestException;
import com.mydegree.renty.exceptions.InternalServerError;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.abstracts.AbstractService;
import com.mydegree.renty.service.abstracts.IUserService;
import com.mydegree.renty.service.helper.UserDetailsTransformer;
import com.mydegree.renty.service.helper.UserTransformer;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;
import com.mydegree.renty.utils.Base64Utils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl extends AbstractService implements IUserService {
    private final IUserDetailsRepository userDetailsRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(IUserDetailsRepository userDetailsRepository,
                           IUserRepository userRepository,
                           IRoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userDetailsRepository = userDetailsRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param userDetails UserDetailsDTO to be created
     * When a user is created by anonymous user, then the default role will be RENTER role with the corresponding permissions
     */
    @Override
    public void saveUserAnon(UserDetailsDTO userDetails) {
        final UserDTO userDTO = userDetails.getUser();
        final String username = userDTO.getUsername();
        final UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity != null) {
            throw new BadRequestException("Username: " + username + " is already taken! Try another one.");
        }
        final RoleEntity roleEntity = roleRepository.findRoleEntityByRole("RENTER");
        if (roleEntity == null) {
            throw new InternalServerError("Role RENTER not found!");
        }
        final UserEntity user = UserTransformer.transformUser(userDTO);
        final String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(Base64Utils.decode(password)));
        userRepository.save(user);
        userDetailsRepository.save(UserDetailsTransformer.transformUserDetails(userDetails));
    }

    @Override
    public UserDetailsDTO findUserDetailsById(Long id) {
        final Optional<UserDetailsEntity> userOptional = userDetailsRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Cannot find user with id: " + id);
        }
        return UserDetailsTransformer.transformUserDetailsEntity(userOptional.get());
    }

    @Override
    public UserDetailsDTO updateUser(UserDetailsDTO userDetails) {
        final Optional<UserDetailsEntity> userOptional = userDetailsRepository.findById(userDetails.getId());
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Cannot find user with id: " + userDetails.getId());
        }
        final UserDetailsEntity userDetailsEntity = userDetailsRepository.save(UserDetailsTransformer.transformUserDetails(userDetails));
        return UserDetailsTransformer.transformUserDetailsEntity(userDetailsEntity);
    }
}
