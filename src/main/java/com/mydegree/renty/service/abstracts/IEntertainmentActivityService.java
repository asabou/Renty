package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import com.mydegree.renty.service.model.EntertainmentActivityOutputDTO;

import java.util.List;

public interface IEntertainmentActivityService {
    List<EntertainmentActivityDTO> findEntertainmentActivitiesByEntertainmentPlaceId(final Long id);
    void saveEntertainmentActivity(final EntertainmentActivityDTO entertainmentActivity);
    List<EntertainmentActivityOutputDTO> findEntertainmentActivitiesDetailsByEntertainmentPlaceId(final Long id);
    EntertainmentActivityDTO findEntertainmentActivityById(final Long id);
}
