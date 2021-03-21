package com.mydegree.renty.service.impl.factories;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.custom.CustomEntertainmentActivity;
import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import com.mydegree.renty.service.model.EntertainmentActivityInputDTO;
import com.mydegree.renty.service.model.EntertainmentActivityPlaceIdDTO;
import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import com.mydegree.renty.service.model.EntertainmentActivityInputDTO;

import java.util.Arrays;
import java.util.List;

public class EntertainmentActivityFactory {
    public EntertainmentActivityEntity createSimpleMockEntertainmentActivity(final String activity) {
        final EntertainmentActivityEntity entertainmentActivity = new EntertainmentActivityEntity();
        entertainmentActivity.setName(activity);
        return entertainmentActivity;
    }

    public List<EntertainmentActivityEntity> createMockEntertainmentActivities() {
        return Arrays.asList(createSimpleMockEntertainmentActivity("football"), createSimpleMockEntertainmentActivity("basket"),
                createSimpleMockEntertainmentActivity("handball"));
    }

    public EntertainmentActivityDTO createSimpleMockEntertainmentActivityDTO(final String activity) {
        final EntertainmentActivityDTO entertainmentActivity = new EntertainmentActivityDTO();
        entertainmentActivity.setName(activity);
        return entertainmentActivity;
    }

    public EntertainmentActivityInputDTO createMockEntertainmentActivityInputDTO() {
        final EntertainmentActivityInputDTO activity = new EntertainmentActivityInputDTO();
        activity.setEntertainmentActivityId(1L);
        activity.setEntertainmentPlaceId(1L);
        activity.setPrice(40.0);
        activity.setMaxPeopleAllowed(12);
        return activity;
    }

    public CustomEntertainmentActivity createMockCustomEntertainmentActivity() {
        final CustomEntertainmentActivity activity = new CustomEntertainmentActivity();
        activity.setEntertainmentActivity("activity");
        activity.setEntertainmentActivityDescription("description");
        activity.setNrReservations(10L);
        return activity;
    }

    public List<CustomEntertainmentActivity> createMockCustomEntertainmentActivities() {
        return Arrays.asList(createMockCustomEntertainmentActivity());
    }

    public EntertainmentActivityPlaceEntity createMockEntertainmentActivityPlaceEntity() {
        final EntertainmentActivityPlaceEntity entity = new EntertainmentActivityPlaceEntity();
        entity.setEntertainmentPlace(new EntertainmentPlaceFactory().createSimpleMockEntertainmentPlaceEntity("place"));
        entity.setEntertainmentActivity(createSimpleMockEntertainmentActivity("activity"));
        return entity;
    }

    public EntertainmentActivityPlaceIdDTO createMockEntertainmentActivityPlaceIdDTO() {
        final EntertainmentActivityPlaceIdDTO entity = new EntertainmentActivityPlaceIdDTO();
        entity.setEntertainmentPlace(1L);
        entity.setEntertainmentActivity(1L);
        return entity;
    }
}
