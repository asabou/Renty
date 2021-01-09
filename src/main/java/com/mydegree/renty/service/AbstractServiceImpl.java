package com.mydegree.renty.service;

import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.IUserRepository;
import com.mydegree.renty.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public abstract class AbstractServiceImpl implements IAbstractService {
    protected final IUserRepository userRepository;

    public AbstractServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Because of the cascade type, all dependent entities will be removed
     * @param username String
     */
    @Override
    public void deleteUserByUsername(final String username) {
        final UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity == null) {
            throw new NotFoundException("Cannot find user with username: " + username);
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
            throw new NotFoundException("Cannot find user with id: " + id);
        }
        userRepository.delete(userEntity.get());
    }
}
