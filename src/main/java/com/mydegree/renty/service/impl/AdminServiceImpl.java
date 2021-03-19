package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.service.abstracts.AbstractService;
import com.mydegree.renty.service.abstracts.IAdminService;
import com.mydegree.renty.service.helper.UserDetailsTransformer;
import com.mydegree.renty.service.model.UserDetailsDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends AbstractService implements IAdminService {

    public AdminServiceImpl(IUserRepository userRepository, IUserDetailsRepository userDetailsRepository, IRoleRepository roleRepository,
                            IEntertainmentActivityRepository entertainmentActivityRepository, PasswordEncoder passwordEncoder,
                            IReservationRepository reservationRepository) {
        super(userRepository, userDetailsRepository, roleRepository, entertainmentActivityRepository, passwordEncoder, reservationRepository);
    }


    @Override
    public void updateRolesForUser(UserDetailsDTO userDetails) {
        final UserEntity userEntity = userRepository.findUserByUsername(userDetails.getUser().getUsername());
        if (userEntity == null) {
            throwNotFoundException("User not found!");
        }
        final UserDetailsEntity userDetailsEntity = UserDetailsTransformer.transformUserDetails(userDetails);
        userEntity.setAuthorities(userDetailsEntity.getUser().getAuthorities());
        userRepository.save(userEntity);
    }
}
