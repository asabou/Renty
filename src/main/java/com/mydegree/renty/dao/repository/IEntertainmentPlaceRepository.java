package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.EntertainmentPlaceEntity;
import com.mydegree.renty.dao.entity.custom.CustomEntertainmentPlace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface IEntertainmentPlaceRepository extends CrudRepository<EntertainmentPlaceEntity, Long> {
    Iterable<EntertainmentPlaceEntity> findEntertainmentPlaceEntitiesByUserDetailsId(final Long id);

    @Query("select ep from EntertainmentPlaceEntity ep where lower(ep.name) like ?1")
    Iterable<EntertainmentPlaceEntity> findEntertainmentPlacesByName(final String name);

    @Query("select ep from EntertainmentPlaceEntity ep inner join EntertainmentActivityPlaceEntity eap on ep.id = eap.entertainmentPlace.id where " +
            "lower(eap.entertainmentActivity.name) like ?1")
    Iterable<EntertainmentPlaceEntity> findEntertainmentPlacesByEntertainmentActivity(final String activity);

    @Query("select ep from EntertainmentPlaceEntity ep inner join  EntertainmentActivityPlaceEntity eap on ep.id = eap.entertainmentPlace.id where " +
            "lower(eap.entertainmentActivity.name) like ?1 and lower(ep.name) like ?2")
    Iterable<EntertainmentPlaceEntity> fineEntertainmentPlacesByActivityAndName(final String activity, final String name);

    EntertainmentPlaceEntity findEntertainmentPlaceEntityByName(final String name);

    @Query("select new com.mydegree.renty.dao.entity.custom.CustomEntertainmentPlace(ep.name, count(res.entertainmentActivityPlace" +
            ".entertainmentPlace)) from ReservationEntity res " +
            "left join EntertainmentPlaceEntity ep on res.entertainmentActivityPlace.entertainmentPlace = ep " +
            "inner join UserDetailsEntity u on ep.userDetails.id = u.id " +
            "where u.id = :userId and res.reservationDate between :dateFrom and :dateTo group by res.entertainmentActivityPlace.entertainmentPlace" +
            " order by count(res.entertainmentActivityPlace.entertainmentPlace) desc ")
    Iterable<CustomEntertainmentPlace> findTopMostRentedEntertainmentPlacesForOwnerBetweenDates(final Long userId, final Date dateFrom,
                                                                                                final Date dateTo);

    @Query("select new com.mydegree.renty.dao.entity.custom.CustomEntertainmentPlace(ep.name, count(res.entertainmentActivityPlace" +
            ".entertainmentPlace)) from ReservationEntity res " +
            "left join EntertainmentPlaceEntity ep on res.entertainmentActivityPlace.entertainmentPlace = ep " +
            "where res.reservationDate between :dateFrom and :dateTo group by res.entertainmentActivityPlace.entertainmentPlace " +
            "order by count(res.entertainmentActivityPlace.entertainmentPlace) desc ")
    Iterable<CustomEntertainmentPlace> findTopMostRentedEntertainmentPlacesForAdminBetweenDates(final Date dateTo, final Date dateFrom);
}
