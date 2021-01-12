package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.EntertainmentPlaceEntity;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.helper.AddressTransformer;
import com.mydegree.renty.service.helper.EntertainmentPlaceTransformer;
import com.mydegree.renty.service.model.EntertainmentPlaceDTO;
import com.mydegree.renty.service.model.EntertainmentPlaceInputDTO;
import com.mydegree.renty.utils.ServicesUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EntertainmentPlaceServiceImpl implements IEntertainmentPlaceService {
    private final IEntertainmentPlaceRepository entertainmentPlaceRepository;
    private final IUserDetailsRepository userDetailsRepository;
    private final IEntertainmentActivityRepository entertainmentActivityRepository;
    private final IAddressRepository addressRepository;
    private final IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository;

    public EntertainmentPlaceServiceImpl(IEntertainmentPlaceRepository entertainmentPlaceRepository, IUserDetailsRepository userDetailsRepository, IEntertainmentActivityRepository entertainmentActivityRepository, IAddressRepository addressRepository, IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository) {
        this.entertainmentPlaceRepository = entertainmentPlaceRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.entertainmentActivityRepository = entertainmentActivityRepository;
        this.addressRepository = addressRepository;
        this.entertainmentActivityPlaceRepository = entertainmentActivityPlaceRepository;
    }

    @Override
    public List<EntertainmentPlaceDTO> findAllEntertainmentPlaces() {
        final Iterable<EntertainmentPlaceEntity> entities = entertainmentPlaceRepository.findAll();
        return EntertainmentPlaceTransformer.transformEntertainmentPlaceEntities(entities);
    }

    @Override
    public List<EntertainmentPlaceDTO> findAllEntertainmentPlacesForAnOwnerId(Long id) {
        final Iterable<EntertainmentPlaceEntity> entities = entertainmentPlaceRepository.findEntertainmentPlaceEntitiesByUserDetailsId(id);
        return EntertainmentPlaceTransformer.transformEntertainmentPlaceEntities(entities);
    }

    @Override
    public List<EntertainmentPlaceDTO> findAllEntertainmentPlacesByAddressOrNameOrDescriptionOrUserDetailsFirstNameOrUserDetailsLastName(String string) {
        final String county = ServicesUtils.wildCardParam(string);
        final String city = ServicesUtils.wildCardParam(string);
        final String street = ServicesUtils.wildCardParam(string);
        final String name = ServicesUtils.wildCardParam(string);
        final String description = ServicesUtils.wildCardParam(string);
        final String firstName = ServicesUtils.wildCardParam(string);
        final String lastName = ServicesUtils.wildCardParam(string);
        final Iterable<EntertainmentPlaceEntity> entities =
                entertainmentPlaceRepository.findEntertainmentPlaceEntitiesByAddressCountyLikeOrAddressCityLikeOrAddressStreetLikeOrNameLikeOrDescriptionLikeOrUserDetailsFirstNameLikeOrUserDetailsLastNameLike(county, city, street, name, description, firstName, lastName);
        return EntertainmentPlaceTransformer.transformEntertainmentPlaceEntities(entities);
    }

    @Transactional
    @Override
    public void saveEntertainmentPlace(EntertainmentPlaceInputDTO entertainmentPlace) {
        final Long userDetailsId = entertainmentPlace.getUserDetailsId();
        final UserDetailsEntity userDetails = findUserDetailsById(userDetailsId);
        createEntertainmentActivitiesIfTheyDoesNotExist(entertainmentPlace.getEntertainmentActivities());
        final EntertainmentPlaceEntity entertainmentPlaceEntity = createEntertainmentPlaceEntityFromInput(entertainmentPlace);
        entertainmentPlaceEntity.setUserDetails(userDetails);
        userDetails.getEntertainmentPlaces().add(entertainmentPlaceEntity);
        final Set<EntertainmentActivityPlaceEntity> entertainmentActivityPlaceEntities =
                createEntertainmentActivityPlaceEntities(entertainmentPlace, entertainmentPlaceEntity);
        entertainmentPlaceEntity.setEntertainmentActivityPlaceEntities(entertainmentActivityPlaceEntities);
        userDetailsRepository.save(userDetails);
        entertainmentPlaceRepository.save(entertainmentPlaceEntity);
        addressRepository.save(entertainmentPlaceEntity.getAddress());
        entertainmentActivityPlaceRepository.saveAll(entertainmentActivityPlaceEntities);
    }

    @Override
    public void deleteEntertainmentPlaceByName(String name) {
        final EntertainmentPlaceEntity entity = entertainmentPlaceRepository.findEntertainmentPlaceEntityByName(name);
        if (entity == null) {
            throw new NotFoundException("Entertainment place not found!");
        }
        entertainmentPlaceRepository.delete(entity);
    }

    @Override
    public void deleteEntertainmentPlaceById(Long id) {
        final Optional<EntertainmentPlaceEntity> entity = entertainmentPlaceRepository.findById(id);
        if (entity.isEmpty()) {
            throw new NotFoundException("Entertainment place not found!");
        }
        entertainmentPlaceRepository.delete(entity.get());
    }

    private UserDetailsEntity findUserDetailsById(final Long id) {
        Optional<UserDetailsEntity> user = userDetailsRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found!");
        }
        return user.get();
    }

    private void createEntertainmentActivitiesIfTheyDoesNotExist(final List<String> activities) {
        activities.forEach((activity) -> {
            final EntertainmentActivityEntity entity = entertainmentActivityRepository.findEntertainmentActivityEntityByName(activity);
            if (entity == null) {
                final EntertainmentActivityEntity entertainmentActivityEntity = new EntertainmentActivityEntity();
                entertainmentActivityEntity.setName(activity);
                entertainmentActivityRepository.save(entertainmentActivityEntity);
            }
        });
    }

    private EntertainmentPlaceEntity createEntertainmentPlaceEntityFromInput(final EntertainmentPlaceInputDTO entertainmentPlace) {
        final EntertainmentPlaceEntity entertainmentPlaceEntity = new EntertainmentPlaceEntity();
        entertainmentPlaceEntity.setName(entertainmentPlaceEntity.getName());
        entertainmentPlaceEntity.setDescription(entertainmentPlace.getDescription());
        entertainmentPlaceEntity.setProfileImage(entertainmentPlace.getProfileImage());
        entertainmentPlaceEntity.setAddress(AddressTransformer.transformAddress(entertainmentPlace.getAddress()));
        return entertainmentPlaceEntity;
    }

    private Set<EntertainmentActivityPlaceEntity> createEntertainmentActivityPlaceEntities(final EntertainmentPlaceInputDTO entertainmentPlace,
                                                                                            final EntertainmentPlaceEntity entertainmentPlaceEntity) {
        final Set<EntertainmentActivityPlaceEntity> set = new HashSet<>();
        for (final String activity : entertainmentPlace.getEntertainmentActivities()) {
            final EntertainmentActivityEntity entertainmentActivityEntity =
                    entertainmentActivityRepository.findEntertainmentActivityEntityByName(activity);
            final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity = new EntertainmentActivityPlaceEntity();
            entertainmentActivityPlaceEntity.setEntertainmentActivity(entertainmentActivityEntity);
            entertainmentActivityPlaceEntity.setEntertainmentPlace(entertainmentPlaceEntity);
            set.add(entertainmentActivityPlaceEntity);
        }
        return set;
    }
}
