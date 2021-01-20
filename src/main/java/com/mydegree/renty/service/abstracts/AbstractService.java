package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.IEntertainmentActivityRepository;
import com.mydegree.renty.dao.repository.IRoleRepository;
import com.mydegree.renty.dao.repository.IUserDetailsRepository;
import com.mydegree.renty.dao.repository.IUserRepository;
import com.mydegree.renty.exceptions.BadRequestException;
import com.mydegree.renty.exceptions.InternalServerErrorException;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.helper.EntertainmentActivityTransformer;
import com.mydegree.renty.service.helper.UserDetailsTransformer;
import com.mydegree.renty.service.helper.UserTransformer;
import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;
import com.mydegree.renty.utils.Base64Utils;
import com.mydegree.renty.utils.ServicesUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public abstract class AbstractService implements IAbstractService {
    protected final IUserRepository userRepository;
    protected final IUserDetailsRepository userDetailsRepository;
    protected final IRoleRepository roleRepository;
    protected final IEntertainmentActivityRepository entertainmentActivityRepository;
    protected final PasswordEncoder passwordEncoder;

    public AbstractService(IUserRepository userRepository, IUserDetailsRepository userDetailsRepository,
                           IRoleRepository roleRepository,
                           IEntertainmentActivityRepository entertainmentActivityRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.roleRepository = roleRepository;
        this.entertainmentActivityRepository = entertainmentActivityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Because of the cascade type, all dependent entities will be removed
     * @param username String
     */
    @Override
    public void deleteUserByUsername(final String username) {
        final UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity == null) {
            throwNotFoundException("Cannot find user with username: " + username);
        }
        userRepository.delete(userEntity);
    }

    /**
     * Because of the cascade type, all dependent entities will be removed
     * @param id Long
     */
    @Override
    public void deleteUserById(final Long id) {
        final Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isEmpty()) {
            throwNotFoundException("Cannot find user with id: " + id);
        }
        userRepository.delete(userEntity.get());
    }

    @Override
    public void createUser(UserDetailsDTO userDetails) {
        final UserDTO userDTO = userDetails.getUser();
        final String username = userDTO.getUsername();
        final UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity != null) {
            throwBadRequestException("This user already exists!");
        }
        final UserEntity user = UserTransformer.transformUser(userDTO);
        final String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(Base64Utils.decode(password)));
        userRepository.save(user);
        userDetailsRepository.save(UserDetailsTransformer.transformUserDetails(userDetails));
    }

    @Override
    public void updateUser(UserDetailsDTO userDetails) {
        final UserDTO userDTO = userDetails.getUser();
        final String username = userDTO.getUsername();
        final UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity == null) {
            throwNotFoundException("User with username: " + username + " does not exists!");
        }
        userDetailsRepository.save(UserDetailsTransformer.transformUserDetails(userDetails));
    }

    @Override
    public void createEntertainmentActivity(EntertainmentActivityDTO entertainmentActivityDTO) {
        final String name = entertainmentActivityDTO.getName();
        final EntertainmentActivityEntity activity = entertainmentActivityRepository.findEntertainmentActivityEntityByName(name);
        if (activity != null) {
            throwBadRequestException("Activity: " + name + " already exists!");
        }
        entertainmentActivityRepository.save(EntertainmentActivityTransformer.transformEntertainmentActivity(entertainmentActivityDTO));
    }

    @Override
    public void resetPassword(UserDTO user) {
        final String username = user.getUsername();
        final String password = user.getPassword();
        final UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity == null) {
            throwNotFoundException("User with username: " + username + " does not exist");
        }
        //TODO maybe reset with email ?
        if (ServicesUtils.isStringNullOrEmpty(password)) {
            throwBadRequestException("Password is empty!");
        }
        userEntity.setPassword(passwordEncoder.encode(Base64Utils.decode(user.getPassword())));
        userRepository.save(userEntity);
    }

    @Override
    public void throwBadRequestException(String message) {
        throw new BadRequestException(message);
    }

    @Override
    public void throwNotFoundException(String message) {
        throw new NotFoundException(message);
    }

    @Override
    public void throwInternalServerErrorException(String message) {
        throw new InternalServerErrorException(message);
    }

}
