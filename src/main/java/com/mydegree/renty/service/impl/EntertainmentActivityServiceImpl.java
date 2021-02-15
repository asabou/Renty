package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.*;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.service.abstracts.AbstractService;
import com.mydegree.renty.service.abstracts.IEntertainmentActivityService;
import com.mydegree.renty.service.helper.EntertainmentActivityTransformer;
import com.mydegree.renty.service.model.*;
import com.mydegree.renty.utils.Constants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EntertainmentActivityServiceImpl extends AbstractService implements IEntertainmentActivityService {
    private final IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository;
    private final IEntertainmentPlaceRepository entertainmentPlaceRepository;
    private final IEntertainmentActivityRepository entertainmentActivityRepository;

    public EntertainmentActivityServiceImpl(IUserRepository userRepository,
                                            IUserDetailsRepository userDetailsRepository,
                                            IRoleRepository roleRepository,
                                            IEntertainmentActivityRepository entertainmentActivityRepository,
                                            PasswordEncoder passwordEncoder,
                                            IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository,
                                            IReservationRepository reservationRepository,
                                            IEntertainmentPlaceRepository entertainmentPlaceRepository,
                                            IEntertainmentActivityRepository entertainmentActivityRepository1) {
        super(userRepository, userDetailsRepository, roleRepository, entertainmentActivityRepository, passwordEncoder, reservationRepository);
        this.entertainmentActivityPlaceRepository = entertainmentActivityPlaceRepository;
        this.entertainmentPlaceRepository = entertainmentPlaceRepository;
        this.entertainmentActivityRepository = entertainmentActivityRepository1;
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

    @Override
    public List<EntertainmentActivityOutputDTO> findEntertainmentActivitiesDetailsByEntertainmentPlaceId(Long id) {
        final Iterable<EntertainmentActivityPlaceEntity> entities =
                entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntitiesByEntertainmentPlace_Id(id);
        return prepareEntertainmentActivityPlaceForOutput(entities);
    }

    @Override
    public EntertainmentActivityDTO findEntertainmentActivityById(Long id) {
        Optional<EntertainmentActivityEntity> byId = entertainmentActivityRepository.findById(id);
        if (byId.isEmpty()) {
            throwNotFoundException("Entertainment activity not found!");
        }
        return EntertainmentActivityTransformer.transformEntertainmentActivityEntity(byId.get());
    }

    @Override
    public void updateEntertainmentActivityForPlace(EntertainmentActivityInputDTO entertainmentActivityInputDTO) {
        EntertainmentActivityPlaceEntity byId = entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(entertainmentActivityInputDTO.getEntertainmentActivityId(), entertainmentActivityInputDTO.getEntertainmentPlaceId());
        if (byId == null) {
            throwNotFoundException(Constants.ENTITY_NOT_FOUND);
        }
        byId.setPricePerHour(entertainmentActivityInputDTO.getPrice());
        byId.setMaxPeopleAllowed(entertainmentActivityInputDTO.getMaxPeopleAllowed());
        entertainmentActivityPlaceRepository.save(byId);
    }

    @Override
    public EntertainmentActivityInputDTO findEntertainmentActivityForPlace(EntertainmentActivityInputDTO entertainmentActivityInput) {
        EntertainmentActivityPlaceEntity byId =
                entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(entertainmentActivityInput.getEntertainmentActivityId(), entertainmentActivityInput.getEntertainmentPlaceId());
        if (byId == null) {
            throwNotFoundException(Constants.ENTITY_NOT_FOUND);
        }
        final EntertainmentActivityInputDTO entAct = new EntertainmentActivityInputDTO();
        entAct.setEntertainmentPlaceId(entertainmentActivityInput.getEntertainmentPlaceId());
        entAct.setEntertainmentActivityId(entertainmentActivityInput.getEntertainmentActivityId());
        entAct.setPrice(byId.getPricePerHour());
        entAct.setMaxPeopleAllowed(byId.getMaxPeopleAllowed());
        return entAct;
    }

    @Override
    public void deleteEntertainmentActivityForPlace(EntertainmentActivityInputDTO entertainmentActivityInput) {
        EntertainmentActivityPlaceEntity byId =
                entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(entertainmentActivityInput.getEntertainmentActivityId(), entertainmentActivityInput.getEntertainmentPlaceId());
        if (byId == null) {
            throwNotFoundException(Constants.ENTITY_NOT_FOUND);
        }
        deleteAllDependentEntitiesForActivityPlace(entertainmentActivityInput);
        entertainmentActivityPlaceRepository.delete(byId);
    }

    @Override
    public List<EntertainmentActivityDTO> findAll() {
        final Iterable<EntertainmentActivityEntity> entities = entertainmentActivityRepository.findAll();
        return EntertainmentActivityTransformer.transformEntertainmentActivityEntities(entities);
    }

    @Override
    public void saveEntertainmentActivityForPlace(EntertainmentActivityInputDTO entertainmentActivityPlaceDTO) {
        final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity =
                createEntertainmentActivityPlaceEntityFromDTO(entertainmentActivityPlaceDTO);
        entertainmentActivityPlaceRepository.save(entertainmentActivityPlaceEntity);
    }

    private EntertainmentActivityPlaceEntity createEntertainmentActivityPlaceEntityFromDTO(final EntertainmentActivityInputDTO entertainmentActivityPlaceDTO) {
        final EntertainmentActivityPlaceEntity entity = new EntertainmentActivityPlaceEntity();
        entity.setPricePerHour(entertainmentActivityPlaceDTO.getPrice());
        entity.setMaxPeopleAllowed(entertainmentActivityPlaceDTO.getMaxPeopleAllowed());
        final EntertainmentActivityPlaceId id = new EntertainmentActivityPlaceId();
        id.setEntertainmentPlace(entertainmentActivityPlaceDTO.getEntertainmentPlaceId());
        id.setEntertainmentActivity(entertainmentActivityPlaceDTO.getEntertainmentActivityId());
        Optional<EntertainmentActivityPlaceEntity> entertainmentActivityPlaceEntity = entertainmentActivityPlaceRepository.findById(id);
        if (entertainmentActivityPlaceEntity.isPresent()) {
            throwBadRequestException(Constants.ENTITY_ALREADY_EXISTS);
        } else {
            Optional<EntertainmentPlaceEntity> entPlace = entertainmentPlaceRepository.findById(entertainmentActivityPlaceDTO.getEntertainmentPlaceId());
            Optional<EntertainmentActivityEntity> act = entertainmentActivityRepository.findById(entertainmentActivityPlaceDTO.getEntertainmentActivityId());
            if (entPlace.isEmpty() || act.isEmpty()) {
                throwBadRequestException(Constants.ENTITY_NOT_FOUND);
            }
            entity.setEntertainmentActivity(act.get());
            entity.setEntertainmentPlace(entPlace.get());
        }
        return entity;
    }

    private void deleteAllDependentEntitiesForActivityPlace(EntertainmentActivityInputDTO entertainmentActivityInput) {
        EntertainmentActivityPlaceEntity byId =
                entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(entertainmentActivityInput.getEntertainmentActivityId(), entertainmentActivityInput.getEntertainmentPlaceId());
        if (byId != null) {
            final Iterable<ReservationEntity> reservationEntities =
                    reservationRepository.findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceIdAndEntertainmentActivityPlace_EntertainmentActivityId(entertainmentActivityInput.getEntertainmentPlaceId(), entertainmentActivityInput.getEntertainmentActivityId());
            reservationRepository.deleteAll(reservationEntities);
            entertainmentActivityPlaceRepository.delete(byId);
        }
    }

    private List<EntertainmentActivityOutputDTO> prepareEntertainmentActivityPlaceForOutput(final Iterable<EntertainmentActivityPlaceEntity> entities) {
        final List<EntertainmentActivityOutputDTO> list = new ArrayList<>();
        entities.forEach((entity) -> {
            list.add(createEntertainmentActivityOutputFromEntity(entity));
        });
        return list;
    }

    private EntertainmentActivityOutputDTO createEntertainmentActivityOutputFromEntity(final EntertainmentActivityPlaceEntity input) {
        final EntertainmentActivityOutputDTO target = new EntertainmentActivityOutputDTO();
        final EntertainmentActivityEntity activity = input.getEntertainmentActivity();
        target.setEntertainmentActivityId(activity.getId());
        target.setEntertainmentActivityName(activity.getName());
        target.setEntertainmentActivityPrice(input.getPricePerHour());
        target.setMaxPeopleAllowed(input.getMaxPeopleAllowed());
        target.setEntertainmentActivityDescription(activity.getDescription());
        return target;
    }
}
