package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.AddressEntity;
import com.mydegree.renty.service.model.AddressDTO;

public class AddressTransformer {
    private static void fillAddress(final AddressEntity input, final AddressDTO target) {
        target.setId(input.getId());
        target.setCounty(input.getCounty());
        target.setCity(input.getCity());
        target.setStreet(input.getStreet());
        target.setNumber(input.getNumber());
    }

    public static AddressDTO transformAddressEntity(final AddressEntity input) {
        if (input == null) {
            return null;
        }
        final AddressDTO target = new AddressDTO();
        fillAddress(input, target);
        return target;
    }

    private static void fillAddressEntity(final AddressDTO input, final AddressEntity target) {
        target.setId(input.getId());
        target.setCounty(input.getCounty());
        target.setCity(input.getCity());
        target.setStreet(input.getStreet());
        target.setNumber(input.getNumber());
        target.setEntertainmentPlace(EntertainmentPlaceTransformer.transformEntertainmentPlace(input.getEntertainmentPlace()));
    }

    public static AddressEntity transformAddress(final AddressDTO input) {
        if (input == null) {
            return null;
        }
        final AddressEntity target = new AddressEntity();
        fillAddressEntity(input, target);
        return target;
    }

}
