package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.*;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.service.abstracts.AbstractService;
import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.helper.AddressTransformer;
import com.mydegree.renty.service.helper.EntertainmentPlaceTransformer;
import com.mydegree.renty.service.model.EntertainmentPlaceDTO;
import com.mydegree.renty.service.model.EntertainmentPlaceInputDTO;
import com.mydegree.renty.utils.Constants;
import com.mydegree.renty.utils.ServicesUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EntertainmentPlaceServiceImpl extends AbstractService implements IEntertainmentPlaceService {
    private final IEntertainmentPlaceRepository entertainmentPlaceRepository;
    private final IAddressRepository addressRepository;
    private final IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository;
    private final SecretKey secretKey;

    public EntertainmentPlaceServiceImpl(IUserRepository userRepository,
                                         IEntertainmentPlaceRepository entertainmentPlaceRepository,
                                         IUserDetailsRepository userDetailsRepository,
                                         IEntertainmentActivityRepository entertainmentActivityRepository,
                                         IAddressRepository addressRepository,
                                         IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository,
                                         SecretKey secretKey,
                                         IRoleRepository roleRepository,
                                         PasswordEncoder passwordEncoder,
                                         IReservationRepository reservationRepository) {
        super(userRepository, userDetailsRepository, roleRepository, entertainmentActivityRepository, passwordEncoder, reservationRepository);
        this.addressRepository = addressRepository;
        this.entertainmentPlaceRepository = entertainmentPlaceRepository;
        this.entertainmentActivityPlaceRepository = entertainmentActivityPlaceRepository;
        this.secretKey = secretKey;
    }

    @Override
    public List<EntertainmentPlaceDTO> findAllEntertainmentPlaces() {
        final Iterable<EntertainmentPlaceEntity> entities = entertainmentPlaceRepository.findAll();
        return EntertainmentPlaceTransformer.transformEntertainmentPlaceEntities(entities);
    }

    @Override
    public List<EntertainmentPlaceDTO> findAllOwnedEntertainmentPlaces(String token) {
        final Claims claims = ServicesUtils.getClaimsFromTokenUsingSecretKey(token, secretKey);
        final Iterable<EntertainmentPlaceEntity> entities =
                entertainmentPlaceRepository.findEntertainmentPlaceEntitiesByUserDetailsId(Long.parseLong(claims.get(Constants.userId).toString()));
        return EntertainmentPlaceTransformer.transformEntertainmentPlaceEntities(entities);
    }

    @Transactional
    @Override
    public void saveEntertainmentPlace(EntertainmentPlaceInputDTO entertainmentPlace) {
        final Long userDetailsId = entertainmentPlace.getUserDetailsId();
        final UserDetailsEntity userDetails = findUserDetailsById(userDetailsId);
        createEntertainmentActivityIfDoesNotExists(entertainmentPlace.getEntertainmentActivity());
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
    public void deleteEntertainmentPlaceById(Long id) {
        final Optional<EntertainmentPlaceEntity> entity = entertainmentPlaceRepository.findById(id);
        if (entity.isEmpty()) {
            throwNotFoundException(Constants.ENT_PLACE_NOT_FOUND);
        }
        deleteAllDependentEntitiesForEntertainmentPlace(id);
        entertainmentPlaceRepository.delete(entity.get());
    }

    @Override
    public EntertainmentPlaceDTO findById(Long id) {
        final Optional<EntertainmentPlaceEntity> entity = entertainmentPlaceRepository.findById(id);
        if (entity.isEmpty()) {
            throwNotFoundException(Constants.ENT_PLACE_NOT_FOUND);
        }
        return EntertainmentPlaceTransformer.transformEntertainmentPlaceEntity(entity.get());
    }

    @Override
    public void updateEntertainmentPlace(EntertainmentPlaceDTO entertainmentPlaceDTO) {
        final Optional<EntertainmentPlaceEntity> entityOptional = entertainmentPlaceRepository.findById(entertainmentPlaceDTO.getId());
        if (entityOptional.isEmpty()) {
            throwNotFoundException(Constants.ENT_PLACE_NOT_FOUND);
        }
        final EntertainmentPlaceEntity entity = entityOptional.get();
        entity.setName(entertainmentPlaceDTO.getName());
        entity.setDescription(entity.getDescription());
        entity.setProfileImage(entertainmentPlaceDTO.getProfileImage());
        entity.setAddress(AddressTransformer.transformAddress(entertainmentPlaceDTO.getAddress()));
        entertainmentPlaceRepository.save(entity);
    }

    @Override
    public List<EntertainmentPlaceDTO> searchEntertainmentPlacesByName(String name) {
        final String wildCard = ServicesUtils.wildCardParam(name);
        final Iterable<EntertainmentPlaceEntity> entities = entertainmentPlaceRepository.findEntertainmentPlacesByName(wildCard);
        return EntertainmentPlaceTransformer.transformEntertainmentPlaceEntities(entities);
    }

    @Override
    public List<EntertainmentPlaceDTO> searchEntertainmentPlacesByActivity(String activity) {
        final String wildCard = ServicesUtils.wildCardParam(activity);
        final Iterable<EntertainmentPlaceEntity> entities = entertainmentPlaceRepository.findEntertainmentPlacesByEntertainmentActivity(wildCard);
        return EntertainmentPlaceTransformer.transformEntertainmentPlaceEntities(entities);
    }

    @Override
    public List<EntertainmentPlaceDTO> searchEntertainmentPlacesByNameAndActivity(String name, String activity) {
        final String nameWildCard = ServicesUtils.wildCardParam(name);
        final String activityWildCard = ServicesUtils.wildCardParam(activity);
        final Iterable<EntertainmentPlaceEntity> entities = entertainmentPlaceRepository.fineEntertainmentPlacesByActivityAndName(activityWildCard,
                nameWildCard);
        return EntertainmentPlaceTransformer.transformEntertainmentPlaceEntities(entities);
    }

    @Override
    public EntertainmentPlaceDTO findEntertainmentPlaceByName(String name) {
        final EntertainmentPlaceEntity entity = entertainmentPlaceRepository.findEntertainmentPlaceEntityByName(name);
        if (entity == null) {
            throwNotFoundException(Constants.ENTITY_NOT_FOUND);
        }
        return EntertainmentPlaceTransformer.transformEntertainmentPlaceEntity(entity);
    }

    private void deleteAllDependentEntitiesForEntertainmentPlace(final Long id) {
        final Iterable<ReservationEntity> entities =
                reservationRepository.findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceId(id);
        reservationRepository.deleteAll(entities);
    }

    private UserDetailsEntity findUserDetailsById(final Long id) {
        Optional<UserDetailsEntity> user = userDetailsRepository.findById(id);
        if (user.isEmpty()) {
            throwNotFoundException(Constants.USER_NOT_FOUND);
        }
        return user.get();
    }

    private void createEntertainmentActivityIfDoesNotExists(final String activity) {
        final EntertainmentActivityEntity entity = entertainmentActivityRepository.findEntertainmentActivityEntityByName(activity);
        if (entity == null) {
            final EntertainmentActivityEntity entertainmentActivityEntity = new EntertainmentActivityEntity();
            entertainmentActivityEntity.setName(activity);
            entertainmentActivityRepository.save(entertainmentActivityEntity);
        }
    }

    private EntertainmentPlaceEntity createEntertainmentPlaceEntityFromInput(final EntertainmentPlaceInputDTO entertainmentPlace) {
        final EntertainmentPlaceEntity entertainmentPlaceEntity = new EntertainmentPlaceEntity();
        entertainmentPlaceEntity.setName(entertainmentPlace.getName());
        entertainmentPlaceEntity.setDescription(entertainmentPlace.getDescription());
        entertainmentPlaceEntity.setProfileImage(entertainmentPlace.getProfileImage());
        entertainmentPlaceEntity.setAddress(AddressTransformer.transformAddress(entertainmentPlace.getAddress()));
        return entertainmentPlaceEntity;
    }

    private Set<EntertainmentActivityPlaceEntity> createEntertainmentActivityPlaceEntities(final EntertainmentPlaceInputDTO entertainmentPlace,
                                                                                            final EntertainmentPlaceEntity entertainmentPlaceEntity) {
        final Set<EntertainmentActivityPlaceEntity> set = new HashSet<>();
        final String activity = entertainmentPlace.getEntertainmentActivity();
        final EntertainmentActivityEntity entertainmentActivityEntity =
                entertainmentActivityRepository.findEntertainmentActivityEntityByName(activity);
        final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity = new EntertainmentActivityPlaceEntity();
        entertainmentActivityPlaceEntity.setEntertainmentActivity(entertainmentActivityEntity);
        entertainmentActivityPlaceEntity.setEntertainmentPlace(entertainmentPlaceEntity);
        entertainmentActivityPlaceEntity.setMaxPeopleAllowed(entertainmentPlace.getMaxPeopleAllowed());
        entertainmentActivityPlaceEntity.setPricePerHour(entertainmentPlace.getPricePerHour());
        set.add(entertainmentActivityPlaceEntity);
        return set;
    }
}
