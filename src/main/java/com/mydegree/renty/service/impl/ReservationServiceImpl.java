package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceId;
import com.mydegree.renty.dao.entity.ReservationEntity;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.repository.IEntertainmentActivityPlaceRepository;
import com.mydegree.renty.dao.repository.IReservationRepository;
import com.mydegree.renty.dao.repository.IUserDetailsRepository;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.abstracts.IReservationService;
import com.mydegree.renty.service.model.ReservationInputDTO;
import com.mydegree.renty.service.model.ReservationOutputDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements IReservationService {
    private final IReservationRepository reservationRepository;
    private final IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository;
    private final IUserDetailsRepository userDetailsRepository;

    public ReservationServiceImpl(IReservationRepository reservationRepository, IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository, IUserDetailsRepository userDetailsRepository) {
        this.reservationRepository = reservationRepository;
        this.entertainmentActivityPlaceRepository = entertainmentActivityPlaceRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public void saveReservation(ReservationInputDTO reservationInput) {
        final EntertainmentActivityPlaceEntity entertainmentActivityPlace = findEntertainmentActivityPlaceEntityFrom(reservationInput);
        final UserDetailsEntity rentalRepresentative = findUserDetailsEntityFrom(reservationInput);
        final ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setReservationDate(reservationInput.getReservationDate());
        reservationEntity.setReservationHour(reservationInput.getReservationHour());
        reservationEntity.setEntertainmentActivityPlace(entertainmentActivityPlace);
        reservationEntity.setUserDetails(rentalRepresentative);
        reservationRepository.save(reservationEntity);
    }

    @Override
    public void cancelReservation(Long id) {
        Optional<ReservationEntity> entityOptional = reservationRepository.findById(id);
        if (entityOptional.isEmpty()) {
            throw new NotFoundException("Reservation not found!");
        }
        reservationRepository.delete(entityOptional.get());
    }

    @Override
    public List<ReservationOutputDTO> findAllActiveReservationsByUserId(Long id) {
        final Iterable<ReservationEntity> reservationEntities =
                reservationRepository.findReservationEntitiesByUserDetailsIdAndReservationDateIsGreaterThan(id, Timestamp.from(Instant.now()));
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
                reservationRepository.findReservationEntitiesByReservationDateGreaterThan(Timestamp.from(Instant.now()));
        return prepareReservationsForOutput(reservationEntities);
    }

    private List<ReservationOutputDTO> prepareReservationsForOutput(final Iterable<ReservationEntity> entities) {
        final List<ReservationOutputDTO> list = new ArrayList<>();
        if (entities == null) {
            return list;
        }
        entities.forEach((entity) -> list.add(transformReservationEntityIntoReservationOutput(entity)));
        return list;
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

    private UserDetailsEntity findUserDetailsEntityFrom(final ReservationInputDTO reservationInputDTO) {
        final Long userDetailsId = reservationInputDTO.getRentalRepresentativeId();
        final Optional<UserDetailsEntity> entityOptional = userDetailsRepository.findById(userDetailsId);
        if (entityOptional.isEmpty()) {
            throw new NotFoundException("Rental representative not found in our database!");
        }
        return entityOptional.get();
    }

    private EntertainmentActivityPlaceEntity findEntertainmentActivityPlaceEntityFrom(final ReservationInputDTO reservationInput) {
        final Long entertainmentActivityId = reservationInput.getEntertainmentActivityId();
        final Long entertainmentPlaceId = reservationInput.getEntertainmentPlaceId();
        final EntertainmentActivityPlaceId entertainmentActivityPlaceId = new EntertainmentActivityPlaceId(entertainmentActivityId, entertainmentPlaceId);
        final Optional<EntertainmentActivityPlaceEntity> entityOptional = entertainmentActivityPlaceRepository.findById(entertainmentActivityPlaceId);
        if (entityOptional.isEmpty()) {
            throw new NotFoundException("Entertainment place or entertainment activity not found!");
        }
        return entityOptional.get();
    }
}
