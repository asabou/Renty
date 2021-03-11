package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.custom.CustomEntertainmentPlace;
import com.mydegree.renty.service.model.CustomEntertainmentPlaceDTO;

import java.util.ArrayList;
import java.util.List;

public class CustomEntertainmentPlaceTransformer {
    private static void fillCustomEntertainmentPlace(final CustomEntertainmentPlace input, final CustomEntertainmentPlaceDTO target) {
        target.setEntertainmentPlace(input.getEntertainmentPlace());
        target.setNrReservations(input.getNrReservations());
    }

    public static CustomEntertainmentPlaceDTO transformCustomEntertainmentPlace(final CustomEntertainmentPlace input) {
        if (input == null) {
            return null;
        }
        final CustomEntertainmentPlaceDTO target = new CustomEntertainmentPlaceDTO();
        fillCustomEntertainmentPlace(input, target);
        return target;
    }

    public static List<CustomEntertainmentPlaceDTO> transformCustomEntertainmentPlaces(final Iterable<CustomEntertainmentPlace> inputs) {
        final List<CustomEntertainmentPlaceDTO> targets = new ArrayList<>();
        if (inputs != null) {
            inputs.forEach((entity) -> targets.add(transformCustomEntertainmentPlace(entity)));
        }
        return targets;
    }
}
