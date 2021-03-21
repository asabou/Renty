package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.IUserRepository;
import com.mydegree.renty.service.impl.factories.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class LoginServiceImplTest extends AbstractTest {
    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private IUserRepository userRepository;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        loginService = new LoginServiceImpl(userRepository);
        final UserFactory userFactory = new UserFactory();
        mockUser = userFactory.createMockUserAdmin("alexandru");
    }

    @Test
    void whenLoadByUsernameAndUserExists_thenReturnMockUser() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(mockUser);
        final UserDetails user = loginService.loadUserByUsername(mockUser.getUsername());
        assertNotNull(user);
        assertEquals(user.getUsername(), mockUser.getUsername());
        assertEquals(user.getPassword(), user.getPassword());
        assertNotNull(user.getAuthorities());
        assertEquals(user.getAuthorities().size(), 3);
        verify(userRepository, times(1)).findUserByUsername(anyString());
    }

    @Test
    void whenLoadByUsernameAndUserDoesNotExists_thenExceptionIsThrown() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        try {
            loginService.loadUserByUsername(mockUser.getUsername());
        } catch (Exception e) {
            assertTrue(e instanceof UsernameNotFoundException);
        }
        verify(userRepository, times(1)).findUserByUsername(anyString());
    }
}