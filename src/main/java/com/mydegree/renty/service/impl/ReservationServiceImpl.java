package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceId;
import com.mydegree.renty.dao.entity.ReservationEntity;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.service.abstracts.AbstractService;
import com.mydegree.renty.service.abstracts.IReservationService;
import com.mydegree.renty.service.helper.EntertainmentActivityPlaceIdTransformer;
import com.mydegree.renty.service.model.EntertainmentActivityPlaceIdDTO;
import com.mydegree.renty.service.model.ReservationInputDTO;
import com.mydegree.renty.service.model.ReservationOutputDTO;
import com.mydegree.renty.utils.DateUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl extends AbstractService implements IReservationService {
    private final IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository;

    public ReservationServiceImpl(IUserRepository userRepository, IUserDetailsRepository userDetailsRepository,
                                  IRoleRepository roleRepository, IEntertainmentActivityRepository entertainmentActivityRepository,
                                  PasswordEncoder passwordEncoder, IReservationRepository reservationRepository,
                                  IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository) {
        super(userRepository, userDetailsRepository, roleRepository, entertainmentActivityRepository, passwordEncoder, reservationRepository);
        this.entertainmentActivityPlaceRepository = entertainmentActivityPlaceRepository;
    }

    @Override
    public void saveReservation(ReservationInputDTO reservationInput) {
        final Long entertainmentActivityId = reservationInput.getEntertainmentActivityId();
        final Long entertainmentPlaceId = reservationInput.getEntertainmentPlaceId();
        final Date reservationDate = DateUtils.parseStringToSqlDate(reservationInput.getReservationDate());
        final Integer reservationHour = reservationInput.getReservationHour();
        final Long rentalRepresentativeId = reservationInput.getRentalRepresentativeId();
        final EntertainmentActivityPlaceEntity entertainmentActivityPlace = findEntertainmentActivityPlaceEntityFrom(entertainmentActivityId, entertainmentPlaceId);
        final ReservationEntity reservation =
                reservationRepository.findReservationEntityByEntertainmentActivityPlaceAndReservationDateAndReservationHour(entertainmentActivityPlace, reservationDate, reservationHour);
        if (reservation != null) {
            throwBadRequestException("The reservation from the chosen place and activity and date and hour is already done by someone else!");
        }
        final UserDetailsEntity rentalRepresentative = findUserDetailsEntityById(rentalRepresentativeId);
        final ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setReservationDate(reservationDate);
        reservationEntity.setReservationHour(reservationHour);
        reservationEntity.setEntertainmentActivityPlace(entertainmentActivityPlace);
        reservationEntity.setUserDetails(rentalRepresentative);
        reservationRepository.save(reservationEntity);
    }

    @Override
    public void cancelReservation(Long id) {
        Optional<ReservationEntity> entityOptional = reservationRepository.findById(id);
        if (entityOptional.isEmpty()) {
            throwNotFoundException("Reservation not found!");
        }
        reservationRepository.delete(entityOptional.get());
    }

    @Override
    public List<ReservationOutputDTO> findAllActiveReservationsFromRenter(Long userId) {
        final Iterable<ReservationEntity> reservationEntities =
                reservationRepository.findReservationEntitiesByUserDetailsIdAndReservationDateIsGreaterThanEqualOrderByReservationDateAsc(userId, DateUtils.getCurrentDate());
        return prepareReservationsForOutput(reservationEntities);
    }

    @Override
    public List<ReservationOutputDTO> findAllReservations() {
        final Iterable<ReservationEntity> reservationEntities = reservationRepository.findAll();
        return prepareReservationsForOutput(reservationEntities);
    }

    @Override
    public List<ReservationOutputDTO> findAllActiveReservations() {
        final Iterable<ReservationEntity> reservationEntities =
                reservationRepository.findReservationEntitiesByReservationDateGreaterThan(DateUtils.getCurrentDate());
        return prepareReservationsForOutput(reservationEntities);
    }

    @Override
    public List<ReservationOutputDTO> findAllActiveReservationsByEntertainmentActivityPlace(EntertainmentActivityPlaceIdDTO entertainmentActivityPlaceId) {
        final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity = findEntertainmentActivityPlace(entertainmentActivityPlaceId);
        final Iterable<ReservationEntity> reservationEntities =
                reservationRepository.findReservationEntitiesByEntertainmentActivityPlaceAndReservationDateIsGreaterThanEqual(entertainmentActivityPlaceEntity, DateUtils.getCurrentDate());
        return prepareReservationsForOutput(reservationEntities);
    }

    @Override
    public List<ReservationOutputDTO> findAllActiveReservationsFromAnOwner(Long userId) {
        final Iterable<ReservationEntity> reservationEntities =
                reservationRepository.findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlace_UserDetailsIdAndReservationDateIsGreaterThanEqual(userId,
                        DateUtils.getCurrentDate());
        return prepareReservationsForOutput(reservationEntities);
    }

    @Override
    public List<ReservationInputDTO> findAllActiveReservationsByActivityAndPlace(EntertainmentActivityPlaceIdDTO entertainmentActivityPlaceId) {
        final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity = findEntertainmentActivityPlace(entertainmentActivityPlaceId);
        final Iterable<ReservationEntity> reservationEntities =
                reservationRepository.findReservationEntitiesByEntertainmentActivityPlaceAndReservationDateIsGreaterThanEqual(entertainmentActivityPlaceEntity, DateUtils.getCurrentDate());
        return prepareReservationsForScheduler(reservationEntities);
    }

    private List<ReservationInputDTO> prepareReservationsForScheduler(final Iterable<ReservationEntity> entities) {
        final List<ReservationInputDTO> list = new ArrayList<>();
        if (entities == null) {
            return list;
        }
        entities.forEach((entity) -> list.add(transformReservationEntityToReservationForScheduler(entity)));
        return list;
    }

    private EntertainmentActivityPlaceEntity findEntertainmentActivityPlace(EntertainmentActivityPlaceIdDTO entertainmentActivityPlaceId) {
        final Optional<EntertainmentActivityPlaceEntity> entertainmentActivityPlaceEntityOptional =
                entertainmentActivityPlaceRepository.findById(EntertainmentActivityPlaceIdTransformer.transformEntertainmentActivityPlaceId(entertainmentActivityPlaceId));
        if (entertainmentActivityPlaceEntityOptional.isEmpty()) {
            throwNotFoundException("Entertainment activity place not found!");
        }
        return entertainmentActivityPlaceEntityOptional.get();
    }

    private List<ReservationOutputDTO> prepareReservationsForOutput(final Iterable<ReservationEntity> entities) {
        final List<ReservationOutputDTO> list = new ArrayList<>();
        if (entities == null) {
            return list;
        }
        entities.forEach((entity) -> list.add(transformReservationEntityIntoReservationOutput(entity)));
        return list;
    }

    private ReservationInputDTO transformReservationEntityToReservationForScheduler(final ReservationEntity input) {
        final ReservationInputDTO output = new ReservationInputDTO();
        output.setReservationDate(input.getReservationDate().toString());
        output.setReservationHour(input.getReservationHour());
        output.setEntertainmentActivityId(input.getEntertainmentActivityPlace().getEntertainmentActivity().getId());
        output.setEntertainmentPlaceId(input.getEntertainmentActivityPlace().getEntertainmentPlace().getId());
        output.setRentalRepresentativeId(input.getUserDetails().getId());
        return output;
    }

    private ReservationOutputDTO transformReservationEntityIntoReservationOutput(final ReservationEntity input) {
        final ReservationOutputDTO output = new ReservationOutputDTO();
        output.setReservationDate(input.getReservationDate());
        output.setReservationHour(input.getReservationHour());
        output.setEntertainmentActivityName(input.getEntertainmentActivityPlace().getEntertainmentActivity().getName());
        output.setEntertainmentPlaceName(input.getEntertainmentActivityPlace().getEntertainmentPlace().getName());
        output.setId(input.getId());
        return output;
    }

    private UserDetailsEntity findUserDetailsEntityById(final Long userDetailsId) {
        final Optional<UserDetailsEntity> entityOptional = userDetailsRepository.findById(userDetailsId);
        if (entityOptional.isEmpty()) {
            throwNotFoundException("Rental representative not found in our database!");
        }
        return entityOptional.get();
    }

    private EntertainmentActivityPlaceEntity findEntertainmentActivityPlaceEntityFrom(final Long entertainmentActivityId, final Long entertainmentPlaceId) {
        final EntertainmentActivityPlaceId entertainmentActivityPlaceId = new EntertainmentActivityPlaceId(entertainmentActivityId, entertainmentPlaceId);
        final Optional<EntertainmentActivityPlaceEntity> entityOptional = entertainmentActivityPlaceRepository.findById(entertainmentActivityPlaceId);
        if (entityOptional.isEmpty()) {
            throwNotFoundException("Entertainment place or entertainment activity not found!");
        }
        return entityOptional.get();
    }
}
