package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import com.mydegree.renty.dao.repository.IEntertainmentActivityRepository;
import com.mydegree.renty.dao.repository.IRoleRepository;
import com.mydegree.renty.dao.repository.IUserDetailsRepository;
import com.mydegree.renty.dao.repository.IUserRepository;
import com.mydegree.renty.exceptions.BadRequestException;
import com.mydegree.renty.service.abstracts.AbstractService;
import com.mydegree.renty.service.abstracts.IEntertainmentActivityService;
import com.mydegree.renty.service.helper.EntertainmentActivityTransformer;
import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntertainmentActivityServiceImpl extends AbstractService implements IEntertainmentActivityService {

    public EntertainmentActivityServiceImpl(IUserRepository userRepository,
                                            IUserDetailsRepository userDetailsRepository,
                                            IRoleRepository roleRepository,
                                            IEntertainmentActivityRepository entertainmentActivityRepository,
                                            PasswordEncoder passwordEncoder) {
        super(userRepository, userDetailsRepository, roleRepository, entertainmentActivityRepository, passwordEncoder);
    }

    @Override
    public List<EntertainmentActivityDTO> findEntertainmentActivitiesByEntertainmentPlaceId(Long id) {
        final Iterable<EntertainmentActivityEntity> entities =
                entertainmentActivityRepository.findEntertainmentActivityEntitiesByEntertainmentPlaceId(id);
        return EntertainmentActivityTransformer.transformEntertainmentActivityEntities(entities);
    }

    @Override
    public void saveEntertainmentActivity(EntertainmentActivityDTO entertainmentActivity) {
        createEntertainmentActivity(entertainmentActivity);
    }
}
