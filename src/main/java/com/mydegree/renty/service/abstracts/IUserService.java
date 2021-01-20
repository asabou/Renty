package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;

import java.util.List;

public interface IUserService {
    void updateAccount(final UserDetailsDTO userDetails);
    void createAdminUser(final UserDetailsDTO userDetails);
    void createOwnerUser(final UserDetailsDTO userDetails);
    void createRenterUser(final UserDetailsDTO userDetails);
    void deleteAccountByUserId(final Long id);
    void deleteAccountByUsername(final String username);
    List<UserDetailsDTO> findAllUserDetails();
    List<UserDTO> findAllUsers();
    UserDetailsDTO findUserByUserId(final Long id);
    Long findUserDetailsIdByUsername(final String username);
    void deleteAccount(final String token);
}
