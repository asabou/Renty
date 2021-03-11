package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.custom.CustomEntertainmentActivity;
import com.mydegree.renty.dao.entity.custom.CustomEntertainmentPlace;
import com.mydegree.renty.dao.entity.custom.CustomReservationDate;
import com.mydegree.renty.dao.entity.custom.CustomReservationHour;
import com.mydegree.renty.dao.repository.IEntertainmentActivityRepository;
import com.mydegree.renty.dao.repository.IEntertainmentPlaceRepository;
import com.mydegree.renty.dao.repository.IReservationRepository;
import com.mydegree.renty.service.abstracts.IStatisticsService;
import com.mydegree.renty.service.helper.CustomEntertainmentActivityTransformer;
import com.mydegree.renty.service.helper.CustomEntertainmentPlaceTransformer;
import com.mydegree.renty.service.helper.CustomReservationDateTransformer;
import com.mydegree.renty.service.helper.CustomReservationHourTransformer;
import com.mydegree.renty.service.model.*;
import com.mydegree.renty.utils.DateUtils;
import com.mydegree.renty.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.util.List;

@Service
public class StatisticsServiceImpl implements IStatisticsService {
    private final SecretKey secretKey;
    private final IEntertainmentActivityRepository entertainmentActivityRepository;
    private final IEntertainmentPlaceRepository entertainmentPlaceRepository;
    private final IReservationRepository reservationRepository;

    public StatisticsServiceImpl(SecretKey secretKey,
                                 IEntertainmentActivityRepository entertainmentActivityRepository,
                                 IEntertainmentPlaceRepository entertainmentPlaceRepository,
                                 IReservationRepository reservationRepository) {
        this.secretKey = secretKey;
        this.entertainmentActivityRepository = entertainmentActivityRepository;
        this.entertainmentPlaceRepository = entertainmentPlaceRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<CustomEntertainmentActivityDTO> findTopMostRentedEntertainmentActivities(String token, String dateFrom, String dateTo) {
        final Date dateFromSql = DateUtils.getMinTemporalDateFromString(dateFrom);
        final Date dateToSql = DateUtils.getMaxTemporalDateFromString(dateTo);
        Iterable<CustomEntertainmentActivity> entities = null;
        if (TokenUtils.hasAdminRole(token, secretKey)) {
            entities =
                    entertainmentActivityRepository.findTopMostRentedEntertainmentActivitiesForAdminBetweenDates(dateFromSql, dateToSql);
        } else {
            if (TokenUtils.hasOwnerRole(token, secretKey)) {
                entities =
                        entertainmentActivityRepository.findTopMostRentedEntertainmentActivitiesForOwnerBetweenDates(TokenUtils.getUserIdFromTokenUsingSecretKey(token, secretKey), dateFromSql, dateToSql);
            }
        }
        return CustomEntertainmentActivityTransformer.transformCustomEntertainmentActivities(entities);
    }

    @Override
    public List<CustomEntertainmentPlaceDTO> findTopMostRentedEntertainmentPlaces(String token, String dateFrom, String dateTo) {
        final Date dateFromSql = DateUtils.getMinTemporalDateFromString(dateFrom);
        final Date dateToSql = DateUtils.getMaxTemporalDateFromString(dateTo);
        Iterable<CustomEntertainmentPlace> entities = null;
        if (TokenUtils.hasAdminRole(token, secretKey)) {
            entities =
                    entertainmentPlaceRepository.findTopMostRentedEntertainmentPlacesForAdminBetweenDates(dateFromSql, dateToSql);
        } else {
            if (TokenUtils.hasOwnerRole(token, secretKey)) {
                entities =
                        entertainmentPlaceRepository.findTopMostRentedEntertainmentPlacesForOwnerBetweenDates(TokenUtils.getUserIdFromTokenUsingSecretKey(token, secretKey), dateFromSql, dateToSql);
            }
        }
        return CustomEntertainmentPlaceTransformer.transformCustomEntertainmentPlaces(entities);
    }

    @Override
    public List<CustomReservationHourDTO> findTopMostRentedHourReservations(String token, Long placeId, String dateFrom, String dateTo) {
        final Date dateFromSql = DateUtils.getMinTemporalDateFromString(dateFrom);
        final Date dateToSql = DateUtils.getMaxTemporalDateFromString(dateTo);
        Iterable<CustomReservationHour> entities = null;
        if (TokenUtils.hasAdminRole(token, secretKey)) {
            entities = reservationRepository.findTopMostRentedHourForAdminBetweenDates(dateFromSql, dateToSql);
        } else {
            if (TokenUtils.hasOwnerRole(token, secretKey)) {
                final Long userId = TokenUtils.getUserIdFromTokenUsingSecretKey(token, secretKey);
                if (placeId == null) {
                    entities =
                            reservationRepository.findTopMostRentedHourForOwnerBetweenDates(userId, dateFromSql, dateToSql);
                } else {
                    entities = reservationRepository.findTopMostRentedHourForOwnerFromPlaceBetweenDates(userId, placeId, dateFromSql, dateToSql);
                }
            }
        }
        return CustomReservationHourTransformer.transformCustomReservationHours(entities);
    }

    @Override
    public List<CustomReservationDateDTO> findTopMostRentedDateReservations(String token, Long placeId, String dateFrom, String dateTo) {
        Iterable<CustomReservationDate> entities = null;
        if (TokenUtils.hasAdminRole(token, secretKey)) {
            entities = reservationRepository.findTopMostRentedDateForAdmin();
        } else {
            if (TokenUtils.hasOwnerRole(token, secretKey)) {
                final Long userId = TokenUtils.getUserIdFromTokenUsingSecretKey(token, secretKey);
                if (placeId == null) {
                    entities = reservationRepository.findTopMostRentedDateForOwner(userId);
                } else {
                    entities = reservationRepository.findTopMostRentedDateForOwnerFromPlace(userId, placeId);
                }
            }
        }
        return CustomReservationDateTransformer.transformCustomReservationDates(entities);
    }

//    private List<CustomEntertainmentActivityDTO> prepareCustomEntertainmentActivities(final Iterable<CustomEntertainmentActivity> inputs) {
//        final List<CustomEntertainmentActivityDTO> targets = new ArrayList<>();
//        inputs.forEach((entity) -> {
//            targets.add(convertCustomEntertainmentActivityToCustomDTO(entity));
//        });
//        return targets;
//    }
//
//    private CustomEntertainmentActivityDTO convertCustomEntertainmentActivityToCustomDTO(final CustomEntertainmentActivity input) {
//        final CustomEntertainmentActivityDTO target = new CustomEntertainmentActivityDTO();
//        final EntertainmentActivityEntity entertainmentActivity =
//                entertainmentActivityRepository.findById(input.getEntertainmentActivityEntityId()).get();
//        target.setEntertainmentActivity(EntertainmentActivityTransformer.transformEntertainmentActivityEntity(entertainmentActivity));
//        target.setNrReservations(input.getNrReservations());
//        return target;
//    }
//
//    private List<CustomEntertainmentPlaceDTO> prepareCustomEntertainmentPlaces(final Iterable<CustomEntertainmentPlace> inputs) {
//        final List<CustomEntertainmentPlaceDTO> targets = new ArrayList<>();
//        inputs.forEach((entity) -> {
//            targets.add(convertCustomEntertainmentPlaceToCustomDTO(entity));
//        });
//        return targets;
//    }
//
//    private CustomEntertainmentPlaceDTO convertCustomEntertainmentPlaceToCustomDTO(final CustomEntertainmentPlace input) {
//        final CustomEntertainmentPlaceDTO target = new CustomEntertainmentPlaceDTO();
//        final EntertainmentPlaceEntity entertainmentPlace = entertainmentPlaceRepository.findById(input.getEntertainmentPlaceEntityId()).get();
//        target.setEntertainmentPlace(EntertainmentPlaceTransformer.transformEntertainmentPlaceEntity(entertainmentPlace));
//        target.setNrReservations(input.getNrReservations());
//        return target;
//    }
}
