package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceId;
import com.mydegree.renty.dao.entity.EntertainmentPlaceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEntertainmentActivityPlaceRepository extends CrudRepository<EntertainmentActivityPlaceEntity, EntertainmentActivityPlaceId> {
    Iterable<EntertainmentActivityPlaceEntity> findEntertainmentActivityPlaceEntitiesByEntertainmentPlace_Id(final Long id);
    Iterable<EntertainmentActivityPlaceEntity> findEntertainmentActivityPlaceEntitiesByEntertainmentActivityAndEntertainmentPlace(final EntertainmentActivityEntity entertainmentActivityEntity, final EntertainmentPlaceEntity entertainmentPlaceEntity);
}
