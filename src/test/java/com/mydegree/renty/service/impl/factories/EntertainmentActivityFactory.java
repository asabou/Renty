package com.mydegree.renty.service.impl.factories;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
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

}
