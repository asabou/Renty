package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.EntertainmentPlaceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEntertainmentPlaceRepository extends CrudRepository<EntertainmentPlaceEntity, Long> {
    Iterable<EntertainmentPlaceEntity> findEntertainmentPlaceEntitiesByUserDetailsId(final Long id);
    Iterable<EntertainmentPlaceEntity> findEntertainmentPlaceEntitiesByAddressCountyLikeOrAddressCityLikeOrAddressStreetLikeOrNameLikeOrDescriptionLikeOrUserDetailsFirstNameLikeOrUserDetailsLastNameLike(final String county, final String city, final String street, final String name, final String description, final String firstName, final String lastName);
    EntertainmentPlaceEntity findEntertainmentPlaceEntityByName(final String name);
}
