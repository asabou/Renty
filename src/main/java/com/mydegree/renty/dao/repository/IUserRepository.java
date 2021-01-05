package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findUserByUsername(String username);
}
