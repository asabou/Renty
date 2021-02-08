package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.EntertainmentActivityPlaceIdDTO;
import com.mydegree.renty.service.model.ReservationInputDTO;
import com.mydegree.renty.service.model.ReservationOutputDTO;

import java.util.List;

public interface IReservationService {
    void saveReservation(final ReservationInputDTO reservationInput);
    void cancelReservation(final Long id);
    List<ReservationOutputDTO> findAllActiveReservationsFromRenter(final String token);
    List<ReservationOutputDTO> findAllReservations();
    List<ReservationOutputDTO> findAllActiveReservations();
    List<ReservationOutputDTO> findAllActiveReservationsByEntertainmentActivityPlace(final EntertainmentActivityPlaceIdDTO entertainmentActivityPlaceId);
    List<ReservationOutputDTO> findAllActiveReservationsFromAnOwner(final String token);
    List<ReservationInputDTO> findAllActiveReservationsByActivityAndPlace(final EntertainmentActivityPlaceIdDTO entertainmentActivityPlaceId);
}
