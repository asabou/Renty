package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.custom.CustomEntertainmentActivity;
import com.mydegree.renty.dao.entity.custom.CustomEntertainmentPlace;
import com.mydegree.renty.dao.entity.custom.CustomReservationDate;
import com.mydegree.renty.dao.entity.custom.CustomReservationHour;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.service.abstracts.AbstractService;
import com.mydegree.renty.service.abstracts.IStatisticsService;
import com.mydegree.renty.service.helper.CustomEntertainmentActivityTransformer;
import com.mydegree.renty.service.helper.CustomEntertainmentPlaceTransformer;
import com.mydegree.renty.service.helper.CustomReservationDateTransformer;
import com.mydegree.renty.service.helper.CustomReservationHourTransformer;
import com.mydegree.renty.service.model.*;
import com.mydegree.renty.utils.DateUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class StatisticsServiceImpl extends AbstractService implements IStatisticsService {
    private final IEntertainmentPlaceRepository entertainmentPlaceRepository;
    public StatisticsServiceImpl(IUserRepository userRepository,
                                 IUserDetailsRepository userDetailsRepository,
                                 IRoleRepository roleRepository,
                                 IEntertainmentActivityRepository entertainmentActivityRepository,
                                 PasswordEncoder passwordEncoder,
                                 IReservationRepository reservationRepository, IEntertainmentPlaceRepository entertainmentPlaceRepository) {
        super(userRepository, userDetailsRepository, roleRepository, entertainmentActivityRepository, passwordEncoder, reservationRepository);
        this.entertainmentPlaceRepository = entertainmentPlaceRepository;
    }

    @Override
    public List<CustomEntertainmentActivityDTO> findTopMostRentedEntertainmentActivities(Long userId, String dateFrom, String dateTo) {
        final Date dateFromSql = DateUtils.getMinTemporalDateFromString(dateFrom);
        final Date dateToSql = DateUtils.getMaxTemporalDateFromString(dateTo);
        Iterable<CustomEntertainmentActivity> entities = null;
        if (userIsOwner(userId)) {
            entities =
                    entertainmentActivityRepository.findTopMostRentedEntertainmentActivitiesForOwnerBetweenDates(userId, dateFromSql, dateToSql);
        } else {
            if (userIsAdmin(userId)) {
                entities =
                        entertainmentActivityRepository.findTopMostRentedEntertainmentActivitiesForAdminBetweenDates(dateFromSql, dateToSql);
            }
        }
        return CustomEntertainmentActivityTransformer.transformCustomEntertainmentActivities(entities);
    }

    @Override
    public List<CustomEntertainmentPlaceDTO> findTopMostRentedEntertainmentPlaces(Long userId, String dateFrom, String dateTo) {
        final Date dateFromSql = DateUtils.getMinTemporalDateFromString(dateFrom);
        final Date dateToSql = DateUtils.getMaxTemporalDateFromString(dateTo);
        Iterable<CustomEntertainmentPlace> entities = null;
        if (userIsOwner(userId)) {
            entities =
                    entertainmentPlaceRepository.findTopMostRentedEntertainmentPlacesForOwnerBetweenDates(userId, dateFromSql, dateToSql);
        } else {
            if (userIsAdmin(userId)) {
                entities =
                        entertainmentPlaceRepository.findTopMostRentedEntertainmentPlacesForAdminBetweenDates(dateFromSql, dateToSql);
            }
        }
        return CustomEntertainmentPlaceTransformer.transformCustomEntertainmentPlaces(entities);
    }

    @Override
    public List<CustomReservationHourDTO> findTopMostRentedHourReservations(Long userId, Long placeId, String dateFrom, String dateTo) {
        final Date dateFromSql = DateUtils.getMinTemporalDateFromString(dateFrom);
        final Date dateToSql = DateUtils.getMaxTemporalDateFromString(dateTo);
        Iterable<CustomReservationHour> entities = null;
        if (userIsOwner(userId)) {
            if (placeId == null) {
                entities =
                        reservationRepository.findTopMostRentedHourForOwnerBetweenDates(userId, dateFromSql, dateToSql);
            } else {
                entities = reservationRepository.findTopMostRentedHourForOwnerFromPlaceBetweenDates(userId, placeId, dateFromSql, dateToSql);
            }
        } else {
            if (userIsAdmin(userId)) {
                entities = reservationRepository.findTopMostRentedHourForAdminBetweenDates(dateFromSql, dateToSql);
            }
        }
        return CustomReservationHourTransformer.transformCustomReservationHours(entities);
    }

    @Override
    public List<CustomReservationDateDTO> findTopMostRentedDateReservations(Long userId, Long placeId) {
        Iterable<CustomReservationDate> entities = null;
        if (userIsOwner(userId)) {
            if (placeId == null) {
                entities = reservationRepository.findTopMostRentedDatesForOwner(userId);
            } else {
                entities = reservationRepository.findTopMostRentedDatesForOwnerFromPlace(userId, placeId);
            }
        } else {
            if (userIsAdmin(userId)) {
                entities = reservationRepository.findTopMostRentedDatesForAdmin();
            }
        }
        return CustomReservationDateTransformer.transformCustomReservationDates(entities);
    }
}
