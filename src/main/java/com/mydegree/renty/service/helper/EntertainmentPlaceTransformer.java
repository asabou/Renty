package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import com.mydegree.renty.dao.entity.EntertainmentPlaceEntity;
import com.mydegree.renty.service.model.EntertainmentPlaceDTO;

import java.util.ArrayList;
import java.util.List;

public class EntertainmentPlaceTransformer {
    private static void fillEntertainmentPlace(final EntertainmentPlaceEntity input, final EntertainmentPlaceDTO target) {
        target.setId(input.getId());
        target.setName(input.getName());
        target.setDescription(input.getDescription());
        target.setProfileImage(input.getProfileImage());
        target.setUserDetails(UserDetailsTransformer.transformUserDetailsEntity(input.getUserDetails()));
        target.setAddress(AddressTransformer.transformAddressEntity(input.getAddress()));
    }

    public static EntertainmentPlaceDTO transformEntertainmentPlaceEntity(final EntertainmentPlaceEntity input) {
        if (input == null) {
            return null;
        }
        final EntertainmentPlaceDTO target = new EntertainmentPlaceDTO();
        fillEntertainmentPlace(input, target);
        return target;
    }

    public static List<EntertainmentPlaceDTO> transformEntertainmentPlaceEntities(final Iterable<EntertainmentPlaceEntity> entities) {
        final List<EntertainmentPlaceDTO> targets = new ArrayList<>();
        if (entities != null) {
            entities.forEach((entity) -> targets.add(transformEntertainmentPlaceEntity(entity)));
        }
        return targets;
    }

    private static void fillEntertainmentPlaceEntity(final EntertainmentPlaceDTO input, final EntertainmentPlaceEntity target) {
        target.setId(input.getId());
        target.setName(input.getName());
        target.setDescription(input.getDescription());
        target.setProfileImage(input.getProfileImage());
        target.setUserDetails(UserDetailsTransformer.transformUserDetails(input.getUserDetails()));
        target.setAddress(AddressTransformer.transformAddress(input.getAddress()));
    }

    public static EntertainmentPlaceEntity transformEntertainmentPlace(final EntertainmentPlaceDTO input) {
        if (input == null) {
            return null;
        }
        final EntertainmentPlaceEntity target = new EntertainmentPlaceEntity();
        fillEntertainmentPlaceEntity(input, target);
        return target;
    }
}
