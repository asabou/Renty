package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import com.mydegree.renty.dao.repository.IEntertainmentActivityRepository;
import com.mydegree.renty.exceptions.BadRequestException;
import com.mydegree.renty.service.abstracts.IEntertainmentActivityService;
import com.mydegree.renty.service.helper.EntertainmentActivityTransformer;
import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntertainmentActivityServiceImpl implements IEntertainmentActivityService {
    private final IEntertainmentActivityRepository entertainmentActivityRepository;

    public EntertainmentActivityServiceImpl(IEntertainmentActivityRepository entertainmentActivityRepository) {
        this.entertainmentActivityRepository = entertainmentActivityRepository;
    }

    @Override
    public List<EntertainmentActivityDTO> findEntertainmentActivitiesByEntertainmentPlaceId(Long id) {
        final Iterable<EntertainmentActivityEntity> entities =
                entertainmentActivityRepository.findEntertainmentActivityEntitiesByEntertainmentPlaceId(id);
        return EntertainmentActivityTransformer.transformEntertainmentActivityEntities(entities);
    }

    @Override
    public void saveEntertainmentActivity(EntertainmentActivityDTO entertainmentActivity) {
        final EntertainmentActivityEntity activity =
                entertainmentActivityRepository.findEntertainmentActivityEntityByName(entertainmentActivity.getName());
        if (activity != null) {
            throw new BadRequestException("Entertainment activity " + entertainmentActivity.getName() + " already exists!");
        }
        final EntertainmentActivityEntity entity = EntertainmentActivityTransformer.transformEntertainmentActivity(entertainmentActivity);
        entertainmentActivityRepository.save(entity);
    }
}
