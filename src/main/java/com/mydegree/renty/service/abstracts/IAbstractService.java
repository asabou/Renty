package com.mydegree.renty.service.abstracts;

public interface IAbstractService {
    void deleteUserByUsername(final String username);
    void deleteUserById(final Long id);
}
