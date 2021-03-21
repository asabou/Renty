package com.mydegree.renty.service.impl.factories;

import com.mydegree.renty.dao.entity.ReservationEntity;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.entity.custom.CustomReservationDate;
import com.mydegree.renty.dao.entity.custom.CustomReservationHour;
import com.mydegree.renty.service.model.ReservationInputDTO;
import com.mydegree.renty.utils.DateUtils;

import java.util.Arrays;
import java.util.List;

public class ReservationFactory {
    public ReservationEntity createSimpleMockReservationEntity() {
        final ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setReservationDate(DateUtils.getCurrentDate());
        reservationEntity.setReservationHour(12);
        return reservationEntity;
    }

    public List<ReservationEntity> createMockReservationEntities() {
        final ReservationEntity reservationEntity = createSimpleMockReservationEntity();
        reservationEntity.setEntertainmentActivityPlace(new EntertainmentActivityFactory().createMockEntertainmentActivityPlaceEntity());
        final UserDetailsEntity user = new UserFactory().createSimpleMockUserDetailsEntity("name");
        user.setId(1L);
        reservationEntity.setUserDetails(user);
        return Arrays.asList(reservationEntity);
    }

    public ReservationInputDTO createMockReservationInputDTO() {
        final ReservationInputDTO entity = new ReservationInputDTO();
        entity.setEntertainmentPlaceId(1L);
        entity.setEntertainmentActivityId(1L);
        entity.setReservationDate(DateUtils.getCurrentDate().toString());
        entity.setReservationHour(11);
        entity.setRentalRepresentativeId(1L);
        return entity;
    }

    public CustomReservationHour createMockCustomReservationHour() {
        final CustomReservationHour res = new CustomReservationHour();
        res.setReservationHour(11);
        res.setNrReservations(10L);
        return res;
    }

    public List<CustomReservationHour> createMockCustomReservationHours() {
        return Arrays.asList(createMockCustomReservationHour());
    }

    public CustomReservationDate createMockCustomReservationDate() {
        final CustomReservationDate date = new CustomReservationDate();
        date.setReservationDate(DateUtils.getCurrentDate());
        date.setNrReservations(10L);
        return date;
    }

    public List<CustomReservationDate> createMockCustomReservationDates() {
        return Arrays.asList(createMockCustomReservationDate());
        return Arrays.asList(createSimpleMockReservationEntity());
    }
}
