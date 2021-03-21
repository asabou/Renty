package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.dao.entity.*;
import com.mydegree.renty.dao.repository.*;
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
import com.mydegree.renty.utils.Constants;
import com.mydegree.renty.utils.ServicesUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public abstract class AbstractService implements IAbstractService {
    protected final IUserRepository userRepository;
    protected final IUserDetailsRepository userDetailsRepository;
    protected final IRoleRepository roleRepository;
    protected final IEntertainmentActivityRepository entertainmentActivityRepository;
    protected final PasswordEncoder passwordEncoder;
    protected final IReservationRepository reservationRepository;

    public AbstractService(IUserRepository userRepository, IUserDetailsRepository userDetailsRepository,
                           IRoleRepository roleRepository,
                           IEntertainmentActivityRepository entertainmentActivityRepository,
                           PasswordEncoder passwordEncoder, IReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.roleRepository = roleRepository;
        this.entertainmentActivityRepository = entertainmentActivityRepository;
        this.passwordEncoder = passwordEncoder;
        this.reservationRepository = reservationRepository;
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
        deleteAllDependentEntitiesForUserId(id);
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
        final UserDetailsEntity userDetailsEntity = UserDetailsTransformer.transformUserDetails(userDetails);
        final String password = user.getPassword();

        user.setPassword(passwordEncoder.encode(Base64Utils.decode(password)));
        user.setUserDetails(userDetailsEntity);
        userDetailsEntity.setUser(user);
        userRepository.save(user);
        userDetailsRepository.save(userDetailsEntity);
    }

    @Override
    public void updateUser(UserDetailsDTO userDetails) {
        final UserDTO userDTO = userDetails.getUser();
        final String username = userDTO.getUsername();
        final UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity == null) {
            throwNotFoundException("User with username: " + username + " does not exists!");
        }
        final UserDetailsEntity userDetailsEntity = userEntity.getUserDetails();
        userDetailsEntity.setFirstName(userDetails.getFirstName());
        userDetailsEntity.setLastName(userDetails.getLastName());
        userDetailsEntity.setEmail(userDetails.getEmail());
        userDetailsEntity.setTelNumber(userDetails.getTelNumber());
        userDetailsRepository.save(userDetailsEntity);
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

    @Override
    public boolean userIsAdmin(Long userId) {
        final UserEntity userEntity = findUserEntityById(userId);
        return userHasRights(userEntity, "ADMIN");
    }

    @Override
    public boolean userIsOwner(Long userId) {
        final UserEntity userEntity = findUserEntityById(userId);
        return userHasRights(userEntity, "OWNER") && !userHasRights(userEntity, "ADMIN");
    }

    @Override
    public boolean userIdHasRenterRights(Long userId) {
        final UserEntity userEntity = findUserEntityById(userId);
        return userHasRights(userEntity, "RENTER");
    }

    private UserEntity findUserEntityById(final Long userId) {
        final Optional<UserEntity> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            throwNotFoundException(Constants.ENTITY_NOT_FOUND);
        }
        return byId.get();
    }

    private boolean userHasRights(final UserEntity userEntity, final String role) {
        final Set<RoleEntity> roles = userEntity.getAuthorities();
        for (final RoleEntity rol : roles) {
            if (rol.getRole().equals(role)) {
                return true;
            }
        }
        return false;
    }

    private void deleteAllDependentEntitiesForUserId(final Long id) {
        deleteReservationsByUserId(id);
    }

    private void deleteReservationsByUserId(final Long userId) {
        final Iterable<ReservationEntity> reservations = reservationRepository.findReservationEntitiesByUserDetailsId(userId);
        reservationRepository.deleteAll(reservations);
    }


}
