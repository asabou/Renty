package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.EntertainmentPlaceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
}
