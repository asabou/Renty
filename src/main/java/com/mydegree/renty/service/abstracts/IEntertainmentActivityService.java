package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import com.mydegree.renty.service.model.EntertainmentActivityInputDTO;
import com.mydegree.renty.service.model.EntertainmentActivityOutputDTO;
import com.mydegree.renty.service.model.EntertainmentPlaceInputDTO;

import java.util.List;

public interface IEntertainmentActivityService {
    List<EntertainmentActivityDTO> findEntertainmentActivitiesByEntertainmentPlaceId(final Long id);
    void saveEntertainmentActivity(final EntertainmentActivityDTO entertainmentActivity);
    List<EntertainmentActivityOutputDTO> findEntertainmentActivitiesDetailsByEntertainmentPlaceId(final Long id);
    EntertainmentActivityDTO findEntertainmentActivityById(final Long id);
    void updateEntertainmentActivityForPlace(final EntertainmentActivityInputDTO entertainmentActivityInputDTO);
    EntertainmentActivityInputDTO findEntertainmentActivityForPlace(final EntertainmentActivityInputDTO entertainmentActivityInput);
    void deleteEntertainmentActivityForPlace(final EntertainmentActivityInputDTO entertainmentActivityInput);
    List<EntertainmentActivityDTO> findAll();
}
