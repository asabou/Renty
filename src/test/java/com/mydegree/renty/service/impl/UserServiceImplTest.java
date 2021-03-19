package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

@Profile("test")
@SpringBootTest
class UserServiceImplTest {
    private UserServiceImpl userService;

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
    private SecretKey secretKey;

    @Mock
    private IReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateAccount() {
    }

    @Test
    void createAdminUser() {
    }

    @Test
    void createOwnerUser() {
    }

    @Test
    void createRenterUser() {
    }

    @Test
    void deleteAccountByUserId() {
    }

    @Test
    void deleteAccountByUsername() {
    }

    @Test
    void findAllUserDetails() {
    }

    @Test
    void findAllUsers() {
    }

    @Test
    void findUserByUserId() {
    }

    @Test
    void findUserDetailsIdByUsername() {
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void findUserDetailsFromToken() {
    }
}