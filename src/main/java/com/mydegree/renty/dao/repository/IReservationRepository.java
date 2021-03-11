package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.ReservationEntity;
import com.mydegree.renty.dao.entity.custom.CustomReservationDate;
import com.mydegree.renty.dao.entity.custom.CustomReservationHour;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface IReservationRepository extends CrudRepository<ReservationEntity, Long> {
    Iterable<ReservationEntity> findReservationEntitiesByUserDetailsIdAndReservationDateIsGreaterThanEqualOrderByReservationDateAsc(final Long userDetailsId,
                                                                                                                                    final Date date);

    Iterable<ReservationEntity> findReservationEntitiesByReservationDateGreaterThan(final Date date);

    ReservationEntity findReservationEntityByEntertainmentActivityPlaceAndReservationDateAndReservationHour(final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity,
                                                                                                            final Date date,
                                                                                                            final Integer hour);
    Iterable<ReservationEntity> findReservationEntitiesByEntertainmentActivityPlaceAndReservationDateIsGreaterThanEqual(final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity, final Date date);

    Iterable<ReservationEntity> findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlace_UserDetailsIdAndReservationDateIsGreaterThanEqual(final Long id, final Date date);

    Iterable<ReservationEntity> findReservationEntitiesByUserDetailsId(final Long id);

    Iterable<ReservationEntity> findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceId(final Long id);

    Iterable<ReservationEntity> findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceIdAndEntertainmentActivityPlace_EntertainmentActivityId(final Long placeId, final Long actId);

    @Query("select new com.mydegree.renty.dao.entity.custom.CustomReservationDate(res.reservationDate, count(res.reservationDate)) from " +
            "ReservationEntity res " +
            "inner join EntertainmentPlaceEntity ep on res.entertainmentActivityPlace.entertainmentPlace = ep " +
            "inner join UserDetailsEntity u on ep.userDetails.id = u.id " +
            "where u.id = :userId group by res.reservationDate order by count(res.reservationDate) desc")
    Iterable<CustomReservationDate> findTopMostRentedDateForOwner(final Long userId);

    @Query("select new com.mydegree.renty.dao.entity.custom.CustomReservationDate(res.reservationDate, count(res.reservationDate)) from " +
            "ReservationEntity res  " +
            "inner join EntertainmentPlaceEntity ep on res.entertainmentActivityPlace.entertainmentPlace = ep " +
            "inner join UserDetailsEntity u on ep.userDetails.id = u.id " +
            "where u.id = :userId and ep.id = :placeId group by res.reservationDate order by count(res.reservationDate) desc")
    Iterable<CustomReservationDate> findTopMostRentedDateForOwnerFromPlace(final Long userId,
                                                                           final Long placeId);

    @Query("select new com.mydegree.renty.dao.entity.custom.CustomReservationDate(res.reservationDate, count(res.reservationDate)) from " +
            "ReservationEntity res group by res.reservationDate order by count(res.reservationDate) desc")
    Iterable<CustomReservationDate> findTopMostRentedDateForAdmin();

    @Query("select new com.mydegree.renty.dao.entity.custom.CustomReservationHour(res.reservationHour, count(res.reservationHour)) from " +
            "ReservationEntity res where res.reservationDate between :dateFrom and :dateTo group by res.reservationHour order by count(res" +
            ".reservationDate) desc ")
    Iterable<CustomReservationHour> findTopMostRentedHourForAdminBetweenDates(final Date dateFrom,
                                                                              final Date dateTo);

    @Query("select new com.mydegree.renty.dao.entity.custom.CustomReservationHour(res.reservationHour, count(res.reservationHour)) from " +
            "ReservationEntity res " +
            "inner join EntertainmentPlaceEntity ep on res.entertainmentActivityPlace.entertainmentPlace = ep " +
            "inner join UserDetailsEntity u on ep.userDetails.id = u.id " +
            "where u.id = :userId and ep.id = :placeId and res.reservationDate between :dateFrom and :dateTo group by res.reservationHour order by " +
            "count(res.reservationHour) desc")
    Iterable<CustomReservationHour> findTopMostRentedHourForOwnerFromPlaceBetweenDates(final Long userId,
                                                                                   final Long placeId,
                                                                                   final Date dateFrom,
                                                                                   final Date dateTo);

    @Query("select new com.mydegree.renty.dao.entity.custom.CustomReservationHour(res.reservationHour, count(res.reservationHour)) from " +
            "ReservationEntity res " +
            "inner join EntertainmentPlaceEntity ep on res.entertainmentActivityPlace.entertainmentPlace = ep " +
            "inner join UserDetailsEntity u on ep.userDetails.id = u.id " +
            "where u.id = :userId and " +
            "res.reservationDate between :dateFrom and :dateTo group by res.reservationHour order by count(res.reservationHour) desc")
    Iterable<CustomReservationHour> findTopMostRentedHourForOwnerBetweenDates(final Long userId,
                                                                              final Date dateFrom,
                                                                              final Date dateTo);
}
