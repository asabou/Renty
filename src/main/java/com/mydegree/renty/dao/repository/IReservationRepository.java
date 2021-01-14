package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.ReservationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface IReservationRepository extends CrudRepository<ReservationEntity, Long> {
    Iterable<ReservationEntity> findReservationEntitiesByUserDetailsIdAndReservationDateIsGreaterThan(final Long userDetailsId,
                                                                                                  final Timestamp timestamp);
    Iterable<ReservationEntity> findReservationEntitiesByReservationDateGreaterThan(final Timestamp timestamp);
}
