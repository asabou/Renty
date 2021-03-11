package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.custom.CustomReservationDate;
import com.mydegree.renty.service.model.CustomReservationDateDTO;

import java.util.ArrayList;
import java.util.List;

public class CustomReservationDateTransformer {
    private static void fillCustomReservationDate(final CustomReservationDate input, final CustomReservationDateDTO target) {
        target.setReservationDate(input.getReservationDate());
        target.setNrReservations(input.getNrReservations());
    }

    public static CustomReservationDateDTO transformCustomReservationDate(final CustomReservationDate input) {
        if (input == null) {
            return null;
        }
        final CustomReservationDateDTO target = new CustomReservationDateDTO();
        fillCustomReservationDate(input, target);
        return target;
    }

    public static List<CustomReservationDateDTO> transformCustomReservationDates(final Iterable<CustomReservationDate> inputs) {
        final List<CustomReservationDateDTO> targets = new ArrayList<>();
        if (inputs != null) {
            inputs.forEach((entity) -> transformCustomReservationDate(entity));
        }
        return targets;
    }
}
