package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.CustomEntertainmentActivityDTO;
import com.mydegree.renty.service.model.CustomEntertainmentPlaceDTO;
import com.mydegree.renty.service.model.CustomReservationDateDTO;
import com.mydegree.renty.service.model.CustomReservationHourDTO;

import java.util.List;

public interface IStatisticsService {
    List<CustomEntertainmentActivityDTO> findTopMostRentedEntertainmentActivities(final Long userId, final String dateFrom, final String dateTo);
    List<CustomEntertainmentPlaceDTO> findTopMostRentedEntertainmentPlaces(final Long userId, final String dateFrom, final String dateTo);
    List<CustomReservationHourDTO> findTopMostRentedHourReservations(final Long userId, final Long placeId, final String dateFrom,
                                                                     final String dateTo);
    List<CustomReservationDateDTO> findTopMostRentedDateReservations(final Long userId, final Long placeId);
}
