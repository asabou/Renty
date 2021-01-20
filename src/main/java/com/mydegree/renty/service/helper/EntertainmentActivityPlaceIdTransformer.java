package com.mydegree.renty.service.helper;

import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceId;
import com.mydegree.renty.service.model.EntertainmentActivityPlaceIdDTO;

public class EntertainmentActivityPlaceIdTransformer {
    private static void fillEntertainmentActivityPlaceId(final EntertainmentActivityPlaceIdDTO input, final EntertainmentActivityPlaceId target) {
        target.setEntertainmentPlace(input.getEntertainmentPlace());
        target.setEntertainmentActivity(input.getEntertainmentActivity());
    }

    public static EntertainmentActivityPlaceId transformEntertainmentActivityPlaceId(final EntertainmentActivityPlaceIdDTO input) {
        if (input == null) {
            return null;
        }
        final EntertainmentActivityPlaceId target = new EntertainmentActivityPlaceId();
        fillEntertainmentActivityPlaceId(input, target);
        return target;
    }
}
