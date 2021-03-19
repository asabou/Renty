package com.mydegree.renty.service.impl.factories;

import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.EntertainmentPlaceEntity;

import java.util.Arrays;
import java.util.List;

public class EntertainmentPlaceFactory {
    public EntertainmentActivityPlaceEntity createSimpleMockEntertainmentActivityPlaceEntity() {
        final EntertainmentActivityPlaceEntity mockEntity = new EntertainmentActivityPlaceEntity();
        mockEntity.setPricePerHour(40.0);
        mockEntity.setMaxPeopleAllowed(12);
        return mockEntity;
    }

    public EntertainmentPlaceEntity createSimpleMockEntertainmentPlaceEntity(final String name) {
        final EntertainmentPlaceEntity place = new EntertainmentPlaceEntity();
        place.setName(name);
        return place;
    }

    public EntertainmentActivityPlaceEntity createMockEntertainmentActivityPlaceEntity(final String activity, final String place) {
        final EntertainmentActivityFactory entertainmentActivityFactory = new EntertainmentActivityFactory();
        final EntertainmentActivityPlaceEntity entity = createSimpleMockEntertainmentActivityPlaceEntity();
        entity.setEntertainmentActivity(entertainmentActivityFactory.createSimpleMockEntertainmentActivity(activity));
        entity.setEntertainmentPlace(createSimpleMockEntertainmentPlaceEntity(place));
        return entity;
    }

    public List<EntertainmentActivityPlaceEntity> createMockEntertainmentActivityPlaceEntities() {
        return Arrays.asList(createMockEntertainmentActivityPlaceEntity("football", "place"));
    }
}
