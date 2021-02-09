package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.ReservationEntity;
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
}
