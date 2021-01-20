package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceId;
import com.mydegree.renty.service.model.EntertainmentActivityPlaceIdDTO;
import com.mydegree.renty.service.model.ReservationInputDTO;
import com.mydegree.renty.service.model.ReservationOutputDTO;

import java.util.List;

public interface IReservationService {
    void saveReservation(final ReservationInputDTO reservationInput);
    void cancelReservation(final Long id);
    List<ReservationOutputDTO> findAllActiveReservationsByUserId(final Long id);
    List<ReservationOutputDTO> findAllReservations();
    List<ReservationOutputDTO> findAllActiveReservations();
    List<ReservationOutputDTO> findAllActiveReservationsByEntertainmentActivityPlace(final EntertainmentActivityPlaceIdDTO entertainmentActivityPlaceId);
}
