package com.mydegree.renty.service.impl.factories;

import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.EntertainmentPlaceEntity;
import com.mydegree.renty.dao.entity.custom.CustomEntertainmentPlace;
import com.mydegree.renty.service.model.EntertainmentPlaceDTO;
import com.mydegree.renty.service.model.EntertainmentPlaceInputDTO;

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

    public List<EntertainmentPlaceEntity> createMockEntertainmentPlaceEntities() {
        return Arrays.asList(createSimpleMockEntertainmentPlaceEntity("place"));
    }

    public EntertainmentPlaceInputDTO createSimpleMockEntertainmentPlaceInputDTO() {
        final EntertainmentPlaceInputDTO entity = new EntertainmentPlaceInputDTO();
        entity.setName("name");
        return entity;
    }

    public EntertainmentPlaceInputDTO createMockEntertainmentPlaceInputDTO() {
        final EntertainmentPlaceInputDTO entity = createSimpleMockEntertainmentPlaceInputDTO();
        return entity;
    }

    public EntertainmentPlaceDTO createSimpleMockEntertainmentPlaceDTO() {
        final EntertainmentPlaceDTO entity = new EntertainmentPlaceDTO();
        entity.setName("place");
        return entity;
    }

    public CustomEntertainmentPlace createMockCustomEntertainmentPlace() {
        final CustomEntertainmentPlace place = new CustomEntertainmentPlace();
        place.setEntertainmentPlace("place");
        place.setNrReservations(10L);
        return place;
    }

    public List<CustomEntertainmentPlace> createMockCustomEntertainmentPlaces() {
        return Arrays.asList(createMockCustomEntertainmentPlace());
    }
}
