package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.service.model.UserDetailsDTO;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsTransformer {
    private static void fillUserDetailsEntity(final UserDetailsDTO input, final UserDetailsEntity target) {
        target.setId(input.getId());
        target.setUser(UserTransformer.transformUser(input.getUser()));
        target.setFirstName(input.getFirstName());
        target.setLastName(input.getLastName());
        target.setEmail(input.getEmail());
        target.setTelNumber(input.getTelNumber());
    }

    private static void fillUserDetails(final UserDetailsEntity input, final UserDetailsDTO target) {
        target.setId(input.getId());
        target.setUser(UserTransformer.transformUserEntity(input.getUser()));
        target.setFirstName(input.getFirstName());
        target.setLastName(input.getLastName());
        target.setEmail(input.getEmail());
        target.setTelNumber(input.getTelNumber());
    }

    public static UserDetailsDTO transformUserDetailsEntity(final UserDetailsEntity entity) {
        if (entity == null) {
            return null;
        }
        final UserDetailsDTO userDetails = new UserDetailsDTO();
        fillUserDetails(entity, userDetails);
        return userDetails;
    }

    public static UserDetailsEntity transformUserDetails(final UserDetailsDTO entity) {
        if (entity == null) {
            return null;
        }
        final UserDetailsEntity userDetails = new UserDetailsEntity();
        fillUserDetailsEntity(entity, userDetails);
        return userDetails;
    }

    public static List<UserDetailsEntity> transformUsersDetails(final List<UserDetailsDTO> usersDetails) {
        final List<UserDetailsEntity> list = new ArrayList<>();
        usersDetails.forEach((userDetails) -> list.add(transformUserDetails(userDetails)));
        return list;
    }

    public static List<UserDetailsDTO> transformUsersDetailsEntities(final List<UserDetailsEntity> usersDetails) {
        final List<UserDetailsDTO> list = new ArrayList<>();
        usersDetails.forEach((userDetails) -> list.add(transformUserDetailsEntity(userDetails)));
        return list;
    }
}
