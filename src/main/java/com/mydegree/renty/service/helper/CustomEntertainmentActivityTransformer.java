package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.custom.CustomEntertainmentActivity;
import com.mydegree.renty.service.model.CustomEntertainmentActivityDTO;

import java.util.ArrayList;
import java.util.List;

public class CustomEntertainmentActivityTransformer {
    private static void fillCustomEntertainmentActivity(final CustomEntertainmentActivity input, final CustomEntertainmentActivityDTO target) {
        target.setEntertainmentActivity(input.getEntertainmentActivity());
        target.setEntertainmentActivityDescription(input.getEntertainmentActivityDescription());
        target.setNrReservations(input.getNrReservations());
    }

    public static CustomEntertainmentActivityDTO transformCustomEntertainmentActivity(final CustomEntertainmentActivity input) {
        if (input == null) {
            return null;
        }
        final CustomEntertainmentActivityDTO target = new CustomEntertainmentActivityDTO();
        fillCustomEntertainmentActivity(input, target);
        return target;
    }

    public static List<CustomEntertainmentActivityDTO> transformCustomEntertainmentActivities(final Iterable<CustomEntertainmentActivity> inputs) {
        final List<CustomEntertainmentActivityDTO> targets = new ArrayList<>();
        if (inputs != null) {
            inputs.forEach((entity) -> targets.add(transformCustomEntertainmentActivity(entity)));
        }
        return targets;
    }
}
