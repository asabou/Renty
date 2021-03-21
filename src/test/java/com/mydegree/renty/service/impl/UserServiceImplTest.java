package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.ReservationEntity;
import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.exceptions.BadRequestException;
import com.mydegree.renty.exceptions.InternalServerErrorException;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.impl.factories.ReservationFactory;
import com.mydegree.renty.service.impl.factories.UserFactory;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;
import com.mydegree.renty.utils.Base64Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceImplTest extends AbstractTest {
    @InjectMocks
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
    @InjectMock
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
    private IReservationRepository reservationRepository;

    private UserFactory userFactory;
    private UserEntity mockUserEntity;
    private RoleEntity mockRoleEntity;
    private ReservationFactory reservationFactory;
    private List<ReservationEntity> mockReservationEntities;
    private List<UserEntity> mockUserEntities;
    private List<UserDetailsEntity> mockUserDetailsEntities;
    private UserDetailsEntity mockUserDetailsEntity;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userDetailsRepository, roleRepository, entertainmentActivityRepository, passwordEncoder, reservationRepository);
        userFactory = new UserFactory();
        mockUserEntity = userFactory.createMockUserOwner("alex");
        mockRoleEntity = userFactory.createMockRole("admin");
        reservationFactory = new ReservationFactory();
        mockReservationEntities = reservationFactory.createMockReservationEntities();
        mockUserEntities = userFactory.createSimpleMockUserEntities();
        mockUserDetailsEntities = userFactory.createSimpleMockUserDetailsEntities();
        mockUserDetailsEntity = userFactory.createSimpleMockUserDetailsEntity("alex");
    }

    @Test
    void whenUpdateAccountAndUserExists_thenReturnNothing() {
        mockUserEntity.setUserDetails(userFactory.createSimpleMockUserDetailsEntity("name"));
        when(userRepository.findUserByUsername(anyString())).thenReturn(mockUserEntity);
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null); //we dont care for the result
        final UserDetailsDTO user = userFactory.createMockOwnerUserDetailsDTO("alex");
        userService.updateAccount(user);
        verify(userRepository, times(1)).findUserByUsername(anyString());
        verify(userDetailsRepository, times(1)).save(any(UserDetailsEntity.class));
    }

    @Test
    void whenUpdateAccountAndUserDoesNotExists_thenExceptionIsThrown() {
        mockUserEntity.setUserDetails(userFactory.createSimpleMockUserDetailsEntity("name"));
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null); //we dont care for the result
        try {
            final UserDetailsDTO user = userFactory.createMockOwnerUserDetailsDTO("alex");
            userService.updateAccount(user);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not exists"));
        }
        verify(userRepository, times(1)).findUserByUsername(anyString());
        verify(userDetailsRepository, times(0)).save(any(UserDetailsEntity.class));
    }

    @Test
    void whenCreateAdminUserAndRolesExistsAndUserDoesNotExists_thenReturnNothing() {
        when(roleRepository.findRoleEntityByRole(anyString())).thenReturn(mockRoleEntity);
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null);
        final UserDetailsDTO user = userFactory.createMockOwnerUserDetailsDTO("user");
        user.getUser().setPassword(Base64Utils.encode("password"));
        userService.createAdminUser(user);
        verify(roleRepository, times(3)).findRoleEntityByRole(anyString());
        verify(userRepository, times(1)).findUserByUsername(anyString());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userDetailsRepository, times(1)).save(any(UserDetailsEntity.class));
    }

    @Test
    void whenCreateAdminUserAndAdminRolesDoesNotExists_thenExceptionIsThrown() {
        when(roleRepository.findRoleEntityByRole(anyString())).thenReturn(null);
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null);
        try {
            final UserDetailsDTO user = userFactory.createMockOwnerUserDetailsDTO("user");
            user.getUser().setPassword(Base64Utils.encode("password"));
            userService.createAdminUser(user);
        } catch (Exception e) {
            assertTrue(e instanceof InternalServerErrorException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(roleRepository, times(3)).findRoleEntityByRole(anyString());
        verify(userRepository, times(0)).findUserByUsername(anyString());
        verify(passwordEncoder, times(0)).encode(any());
        verify(userRepository, times(0)).save(any(UserEntity.class));
        verify(userDetailsRepository, times(0)).save(any(UserDetailsEntity.class));
    }

    @Test
    void whenCreateAdminUserAndUserAlreadyExists_thenExceptionIsThrown() {
        when(roleRepository.findRoleEntityByRole(anyString())).thenReturn(mockRoleEntity);
        when(userRepository.findUserByUsername(anyString())).thenReturn(mockUserEntity);
        when(passwordEncoder.encode(any())).thenReturn("password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null);
        try {
            final UserDetailsDTO user = userFactory.createMockOwnerUserDetailsDTO("user");
            user.getUser().setPassword(Base64Utils.encode("password"));
            userService.createAdminUser(user);
        } catch (Exception e) {
            assertTrue(e instanceof BadRequestException);
            assertTrue(e.getMessage().contains("already exists"));
        }
        verify(roleRepository, times(3)).findRoleEntityByRole(anyString());
        verify(userRepository, times(1)).findUserByUsername(anyString());
        verify(passwordEncoder, times(0)).encode(any());
        verify(userRepository, times(0)).save(any(UserEntity.class));
        verify(userDetailsRepository, times(0)).save(any(UserDetailsEntity.class));
    }

    @Test
    void whenCreateOwnerUserAndRolesExists_thenReturnNothing() {
        when(roleRepository.findRoleEntityByRole(anyString())).thenReturn(mockRoleEntity);
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null);
        final UserDetailsDTO user = userFactory.createMockOwnerUserDetailsDTO("user");
        user.getUser().setPassword(Base64Utils.encode("password"));
        userService.createOwnerUser(user);
        verify(roleRepository, times(2)).findRoleEntityByRole(anyString());
        verify(userRepository, times(1)).findUserByUsername(anyString());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userDetailsRepository, times(1)).save(any(UserDetailsEntity.class));
    }

    @Test
    void whenCreateOwnerUserAndRolesDoesNotExists_thenExceptionIsThrown() {
        when(roleRepository.findRoleEntityByRole(anyString())).thenReturn(null);
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null);
        try {
            final UserDetailsDTO user = userFactory.createMockOwnerUserDetailsDTO("user");
            user.getUser().setPassword(Base64Utils.encode("password"));
            userService.createOwnerUser(user);
        } catch (Exception e) {
            assertTrue(e instanceof InternalServerErrorException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(roleRepository, times(2)).findRoleEntityByRole(anyString());
        verify(userRepository, times(0)).findUserByUsername(anyString());
        verify(passwordEncoder, times(0)).encode(any());
        verify(userRepository, times(0)).save(any(UserEntity.class));
        verify(userDetailsRepository, times(0)).save(any(UserDetailsEntity.class));
    }

    @Test
    void whenCreateRenterUserAndRoleExists_thenReturnNothing() {
        when(roleRepository.findRoleEntityByRole(anyString())).thenReturn(mockRoleEntity);
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null);
        final UserDetailsDTO user = userFactory.createMockOwnerUserDetailsDTO("user");
        user.getUser().setPassword(Base64Utils.encode("password"));
        userService.createRenterUser(user);
        verify(roleRepository, times(1)).findRoleEntityByRole(anyString());
        verify(userRepository, times(1)).findUserByUsername(anyString());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userDetailsRepository, times(1)).save(any(UserDetailsEntity.class));
    }

    @Test
    void whenCreateRenterUserAndRoleDoesNotExists() {
        when(roleRepository.findRoleEntityByRole(anyString())).thenReturn(null);
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null);
        try {
            final UserDetailsDTO user = userFactory.createMockOwnerUserDetailsDTO("user");
            user.getUser().setPassword(Base64Utils.encode("password"));
            userService.createRenterUser(user);
        } catch (Exception e) {
            assertTrue(e instanceof InternalServerErrorException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(roleRepository, times(1)).findRoleEntityByRole(anyString());
        verify(userRepository, times(0)).findUserByUsername(anyString());
        verify(passwordEncoder, times(0)).encode(any());
        verify(userRepository, times(0)).save(any(UserEntity.class));
        verify(userDetailsRepository, times(0)).save(any(UserDetailsEntity.class));
    }

    @Test
    void whenDeleteAccountByUserIdAndUserExists_thenReturnNothing() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUserEntity));
        when(reservationRepository.findReservationEntitiesByUserDetailsId(any())).thenReturn(mockReservationEntities);
        doNothing().when(reservationRepository).deleteAll(anyCollection());
        doNothing().when(userRepository).delete(any());
        userService.deleteAccountByUserId(1L);
        verify(userRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).findReservationEntitiesByUserDetailsId(any());
        verify(reservationRepository, times(1)).deleteAll(anyCollection());
        verify(userRepository, times(1)).delete(any());
    }

    @Test
    void whenDeleteAccountByUserIdAndUserDoesNotExists_thenExceptionIsThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(reservationRepository.findReservationEntitiesByUserDetailsId(any())).thenReturn(mockReservationEntities);
        doNothing().when(reservationRepository).deleteAll(anyCollection());
        doNothing().when(userRepository).delete(any());
        try {
            userService.deleteAccountByUserId(1L);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not find"));
        }
        verify(userRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(0)).findReservationEntitiesByUserDetailsId(any());
        verify(reservationRepository, times(0)).deleteAll(anyCollection());
        verify(userRepository, times(0)).delete(any());
    }

    @Test
    void whenDeleteAccountByUsernameAndUserExists_thenReturnNothing() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(mockUserEntity);
        doNothing().when(userRepository).delete(any(UserEntity.class));
        userService.deleteAccountByUsername("username");
        verify(userRepository, times(1)).findUserByUsername(anyString());
        verify(userRepository, times(1)).delete(any(UserEntity.class));
    }

    @Test
    void whenDeleteAccountByUsernameAndUserDoesNotExists_thenExceptionIsThrown() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        doNothing().when(userRepository).delete(any(UserEntity.class));
        try {
            userService.deleteAccountByUsername("username");
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not find"));
        }
        verify(userRepository, times(1)).findUserByUsername(anyString());
        verify(userRepository, times(0)).delete(any(UserEntity.class));
    }

    @Test
    void whenFindAllUserDetails_thenReturnMockUserDetailsEntities() {
        when(userDetailsRepository.findAll()).thenReturn(mockUserDetailsEntities);
        final List<UserDetailsDTO> users = userService.findAllUserDetails();
        assertNotNull(users);
        assertEquals(users.size(), 1);
        verify(userDetailsRepository, times(1)).findAll();
    }

    @Test
    void whenFindAllUsers_thenReturnMockUserEntities() {
        when(userRepository.findAll()).thenReturn(mockUserEntities);
        final List<UserDTO> users = userService.findAllUsers();
        assertNotNull(users);
        assertEquals(users.size(), 1);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void whenFindUserByUserIdAndUserExists_thenReturnMockUserDetailsEntity() {
        when(userDetailsRepository.findById(anyLong())).thenReturn(Optional.of(mockUserDetailsEntity));
        final UserDetailsDTO user = userService.findUserByUserId(1L);
        assertNotNull(user);
        verify(userDetailsRepository, times(1)).findById(anyLong());
    }

    @Test
    void whenFindUserByUserIdAndUserDoesNotExists_thenExceptionIsThrown(){
        when(userDetailsRepository.findById(anyLong())).thenReturn(Optional.of(mockUserDetailsEntity));
        try {
            final UserDetailsDTO user = userService.findUserByUserId(1L);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not exists"));
        }
        verify(userDetailsRepository, times(1)).findById(anyLong());
    }

    @Test
    void whenFindUserDetailsIdByUsernameAndUserExists_thenReturnMockUserEntity() {
        mockUserEntity.setId(1L);
        when(userRepository.findUserByUsername(anyString())).thenReturn(mockUserEntity);
        final Long userId = userService.findUserDetailsIdByUsername("username");
        assertNotNull(userId);
        assertEquals(userId.longValue(), 1L);
        verify(userRepository, times(1)).findUserByUsername(anyString());
    }

    @Test
    void whenFindUserDetailsIdByUsernameAndUserDoesNotExists_thenExceptionIsThrown() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(mockUserEntity);
        try {
            final Long userId = userService.findUserDetailsIdByUsername("username");
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not exists"));
        }
        verify(userRepository, times(1)).findUserByUsername(anyString());
    }

    @Test
    void whenDeleteAccountAndUserExists_thenReturnNothing() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(mockUserEntity);
        doNothing().when(userRepository).delete(any(UserEntity.class));
        userService.deleteAccountByUsername("username");
        verify(userRepository, times(1)).findUserByUsername(anyString());
        verify(userRepository, times(1)).delete(any(UserEntity.class));
    }

    @Test
    void whenDeleteAccountAndUserDoesNotExists_thenExceptionIsThrown() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        doNothing().when(userRepository).delete(any(UserEntity.class));
        try {
            userService.deleteAccountByUsername("username");
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not find"));
        }
        verify(userRepository, times(1)).findUserByUsername(anyString());
        verify(userRepository, times(0)).delete(any(UserEntity.class));
    }
}