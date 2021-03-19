package com.mydegree.renty.service.impl.factories;

import com.mydegree.renty.dao.entity.ReservationEntity;
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
        return Arrays.asList(createSimpleMockReservationEntity());
    }
}
