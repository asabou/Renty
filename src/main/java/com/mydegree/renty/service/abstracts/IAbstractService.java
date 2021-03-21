package com.mydegree.renty.service.abstracts;

import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;

public interface IAbstractService {
    void deleteUserByUsername(final String username);
    void deleteUserById(final Long id);
    void createUser(final UserDetailsDTO userDetails);
    void updateUser(final UserDetailsDTO userDetails);
    void createEntertainmentActivity(final EntertainmentActivityDTO entertainmentActivity);
    void resetPassword(final UserDTO user);
    void throwBadRequestException(final String message);
    void throwNotFoundException(final String message);
    void throwInternalServerErrorException(final String message);
    boolean userIsAdmin(final Long  userId);
    boolean userIsOwner(final Long userId);
    boolean userIdHasRenterRights(final Long userId);
}
