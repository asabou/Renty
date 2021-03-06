package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.AddressEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressRepository extends CrudRepository<AddressEntity, Long> {
}
