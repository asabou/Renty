package com.mydegree.renty.service;

import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.IUserRepository;
import com.mydegree.renty.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginServiceImplTest extends AbstractTestFactory {
    LoginServiceImpl loginService;
    private UserEntity mockUser;

    @Mock
    IUserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginService = new LoginServiceImpl(userRepository);
        mockUser = createMockUser();
    }

    @Test
    void whenFindUserByUsernameAndUserExists_thenReturnMockUserEntity() {
        Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(mockUser);
        final UserDetails user = loginService.loadUserByUsername(mockUser.getUsername());
        assertNotNull(user);
        assertEquals(user.getPassword(), mockUser.getPassword());
        assertEquals(user.getUsername(), mockUser.getUsername());
        assertNotNull(user.getAuthorities());
        assertEquals(user.getAuthorities().size(), 2);
        Mockito.verify(userRepository, Mockito.times(1)).findUserByUsername(Mockito.anyString());
    }

    @Test
    void whenFindUserByUsernameAndUserDoesNotExists_thenExceptionIsThrown() {
        Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(null);
        try {
            loginService.loadUserByUsername(mockUser.getUsername());
        } catch (Exception e) {
            assertTrue(e instanceof UsernameNotFoundException);
        }
        Mockito.verify(userRepository, Mockito.times(1)).findUserByUsername(Mockito.anyString());
    }
}