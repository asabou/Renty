package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.custom.CustomReservationHour;
import com.mydegree.renty.service.model.CustomReservationHourDTO;

import java.util.ArrayList;
import java.util.List;

public class CustomReservationHourTransformer {
    private static void fillCustomReservationHour(final CustomReservationHour input, final CustomReservationHourDTO target) {
        target.setReservationHour(input.getReservationHour());
        target.setNrReservations(input.getNrReservations());
    }

    public static CustomReservationHourDTO transformCustomReservationHour(final CustomReservationHour input) {
        if (input == null) {
            return null;
        }
        final CustomReservationHourDTO target = new CustomReservationHourDTO();
        fillCustomReservationHour(input, target);
        return target;
    }

    public static List<CustomReservationHourDTO> transformCustomReservationHours(final Iterable<CustomReservationHour> inputs) {
        final List<CustomReservationHourDTO> targets = new ArrayList<>();
        if (inputs != null) {
            inputs.forEach((entity) -> targets.add(transformCustomReservationHour(entity)));
        }
        return targets;
    }

}
