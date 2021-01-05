package com.mydegree.renty.dao.repository;

import com.mydegree.renty.dao.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends CrudRepository<RoleEntity, Long> {
    RoleEntity findRoleEntityByRole(String role);
}
