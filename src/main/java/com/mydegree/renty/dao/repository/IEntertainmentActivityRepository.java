package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import com.mydegree.renty.dao.entity.custom.CustomEntertainmentActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface IEntertainmentActivityRepository extends CrudRepository<EntertainmentActivityEntity, Long> {
    @Query("select ea from EntertainmentActivityEntity ea inner join EntertainmentActivityPlaceEntity eap on ea.id = eap.entertainmentActivity.id " +
            "inner join EntertainmentPlaceEntity ep on eap.entertainmentPlace.id = ep.id where ep.id = :id")

    Iterable<EntertainmentActivityEntity> findEntertainmentActivityEntitiesByEntertainmentPlaceId(final Long id);

    EntertainmentActivityEntity findEntertainmentActivityEntityByName(final String name);

    @Query("select new com.mydegree.renty.dao.entity.custom.CustomEntertainmentActivity(ea.name, ea.description, " +
            "count(res.entertainmentActivityPlace.entertainmentActivity)) from ReservationEntity res " +
            "left join EntertainmentActivityEntity ea on res.entertainmentActivityPlace.entertainmentActivity = ea " +
            "left join EntertainmentPlaceEntity ep on res.entertainmentActivityPlace.entertainmentPlace = ep " +
            "inner join UserDetailsEntity u on ep.userDetails.id = u.id " +
            "where u.id = :userId and res.reservationDate between :dateFrom and :dateTo group by res.entertainmentActivityPlace" +
            ".entertainmentActivity order by count(res.entertainmentActivityPlace.entertainmentActivity) desc ")
    Iterable<CustomEntertainmentActivity> findTopMostRentedEntertainmentActivitiesForOwnerBetweenDates(final Long userId, final Date dateFrom, final Date dateTo);

    @Query("select new com.mydegree.renty.dao.entity.custom.CustomEntertainmentActivity(ea.name, ea.description, " +
            "count(res.entertainmentActivityPlace.entertainmentActivity)) from ReservationEntity res " +
            "left join EntertainmentActivityEntity ea on res.entertainmentActivityPlace.entertainmentActivity = ea " +
            "left join EntertainmentPlaceEntity ep on res.entertainmentActivityPlace.entertainmentPlace = ep " +
            "where res.reservationDate between :dateFrom and :dateTo group by res.entertainmentActivityPlace.entertainmentActivity order by " +
            "count(res.entertainmentActivityPlace.entertainmentActivity) desc ")
    Iterable<CustomEntertainmentActivity> findTopMostRentedEntertainmentActivitiesForAdminBetweenDates(final Date dateFrom, final Date dateTo);
}
