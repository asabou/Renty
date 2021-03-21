package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.impl.factories.UserFactory;
import com.mydegree.renty.service.model.UserDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class AdminServiceImplTest extends AbstractTest {
    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserDetailsRepository userDetailsRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private IEntertainmentActivityRepository entertainmentActivityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    IReservationRepository reservationRepository;

    private UserEntity mockUser;
    private UserDetailsDTO mockUserDetails;
    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        adminService = new AdminServiceImpl(userRepository,
                userDetailsRepository, roleRepository,
                entertainmentActivityRepository, passwordEncoder, reservationRepository);
        userFactory = new UserFactory();
        mockUser = userFactory.createMockUserAdmin("alexandru");
        mockUserDetails = userFactory.createMockOwnerUserDetailsDTO("alexandru");
    }

    @Test
    void whenUpdateRolesForUserAndUserExists_thenReturnNothing() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(mockUser);
        final UserEntity mockUserWithRolesUpdated = userFactory.createMockUserOwner("alexandru");
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUserWithRolesUpdated);
        adminService.updateRolesForUser(mockUserDetails);
        verify(userRepository, times(1)).findUserByUsername(mockUser.getUsername());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void whenUpdateRolesForUserAndUserDoesNotExists_thenExceptionIsThrown() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        final UserEntity mockUserWithRolesUpdated = userFactory.createMockUserOwner("alexandru");
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUserWithRolesUpdated);
        try {
            adminService.updateRolesForUser(mockUserDetails);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(userRepository, times(1)).findUserByUsername(mockUser.getUsername());
        verify(userRepository, times(0)).save(mockUser);
    }
}