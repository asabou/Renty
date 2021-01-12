package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEntertainmentActivityRepository extends CrudRepository<EntertainmentActivityEntity, Long> {
    @Query("select ea from EntertainmentActivityEntity ea inner join EntertainmentActivityPlaceEntity eap on ea.id = eap.entertainmentActivity.id " +
            "inner join EntertainmentPlaceEntity ep on eap.entertainmentPlace.id = ep.id where ep.id = :id")
    Iterable<EntertainmentActivityEntity> findEntertainmentActivityEntitiesByEntertainmentPlaceId(final Long id);
    EntertainmentActivityEntity findEntertainmentActivityEntityByName(final String name);
}
