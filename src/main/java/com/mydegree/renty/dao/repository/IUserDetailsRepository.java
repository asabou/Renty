package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.UserDetailsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDetailsRepository extends CrudRepository<UserDetailsEntity, Long> {
}
