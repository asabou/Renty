package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.ReservationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReservationRepository extends CrudRepository<ReservationEntity, Long> {
}
