package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import com.mydegree.renty.service.model.EntertainmentActivityDTO;

import java.util.ArrayList;
import java.util.List;

public class EntertainmentActivityTransformer {
    private static void fillEntertainmentActivity(final EntertainmentActivityEntity input, final EntertainmentActivityDTO target) {
        target.setId(input.getId());
        target.setDescription(input.getDescription());
        target.setName(input.getName());
    }

    public static EntertainmentActivityDTO transformEntertainmentActivityEntity(final EntertainmentActivityEntity input) {
        if (input == null) {
            return null;
        }
        final EntertainmentActivityDTO target = new EntertainmentActivityDTO();
        fillEntertainmentActivity(input, target);
        return target;
    }

    public static List<EntertainmentActivityDTO> transformEntertainmentActivityEntities(final Iterable<EntertainmentActivityEntity> inputs) {
        final List<EntertainmentActivityDTO> targets = new ArrayList<>();
        if (inputs != null) {
            inputs.forEach((entity) -> targets.add(transformEntertainmentActivityEntity(entity)));
        }
        return targets;
    }

    private static void fillEntertainmentActivityEntity(final EntertainmentActivityDTO input, final EntertainmentActivityEntity target) {
        target.setId(input.getId());
        target.setDescription(input.getDescription());
        target.setName(input.getName());
    }

    public static EntertainmentActivityEntity transformEntertainmentActivity(final EntertainmentActivityDTO input) {
        if (input == null) {
            return null;
        }
        final EntertainmentActivityEntity target = new EntertainmentActivityEntity();
        fillEntertainmentActivityEntity(input, target);
        return target;
    }
}
