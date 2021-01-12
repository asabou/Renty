package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.EntertainmentActivityDTO;

import java.util.List;

public interface IEntertainmentActivityService {
    List<EntertainmentActivityDTO> findEntertainmentActivitiesByEntertainmentPlaceId(final Long id);
    void saveEntertainmentActivity(final EntertainmentActivityDTO entertainmentActivity);
}
