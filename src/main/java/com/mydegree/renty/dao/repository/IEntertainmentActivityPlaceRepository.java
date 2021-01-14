package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEntertainmentActivityPlaceRepository extends CrudRepository<EntertainmentActivityPlaceEntity, EntertainmentActivityPlaceId> {
}
