package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.UserDetailsDTO;

public interface IUserService {
    void saveUserAnon(final UserDetailsDTO userDetails);
    UserDetailsDTO findUserDetailsById(final Long id);
    UserDetailsDTO updateUser(final UserDetailsDTO userDetails);
}
