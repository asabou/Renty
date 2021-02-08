package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.EntertainmentPlaceEntity;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
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
            throwNotFoundException("Entertainment place not found!");
        }
        entertainmentPlaceRepository.delete(entity);
    }

    @Override
    public void deleteEntertainmentPlaceById(Long id) {
        final Optional<EntertainmentPlaceEntity> entity = entertainmentPlaceRepository.findById(id);
        if (entity.isEmpty()) {
            throwNotFoundException("Entertainment place not found!");
        }
        entertainmentPlaceRepository.delete(entity.get());
    }

    @Override
    public EntertainmentPlaceDTO findById(Long id) {
        final Optional<EntertainmentPlaceEntity> entity = entertainmentPlaceRepository.findById(id);
        if (entity.isEmpty()) {
            throwNotFoundException("Entertainment place not found!");
        }
        return EntertainmentPlaceTransformer.transformEntertainmentPlaceEntity(entity.get());
    }

    private UserDetailsEntity findUserDetailsById(final Long id) {
        Optional<UserDetailsEntity> user = userDetailsRepository.findById(id);
        if (user.isEmpty()) {
            throwNotFoundException("User not found!");
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
        entertainmentPlaceEntity.setName(entertainmentPlace.getName());
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
