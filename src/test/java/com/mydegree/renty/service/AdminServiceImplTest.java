package com.mydegree.renty.service;

import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.IUserRepository;
import com.mydegree.renty.exceptions.BadRequestException;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.helper.UserTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AdminServiceImplTest extends AbstractTestFactory {
    AdminServiceImpl adminService;

    private UserEntity mockUser;

    @Mock
    IUserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminService = new AdminServiceImpl(userRepository);
        mockUser = createMockUser();
    }

    @Test
    @DisplayName("Save user")
    void whenUserSaveAndTheUsernameIsNotAlreadyTaken_thenReturnMockUser() {
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(mockUser);
        adminService.saveUser(UserTransformer.transform(mockUser));
        Mockito.verify(userRepository, Mockito.times(1)).findUserByUsername(mockUser.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName("Delete user by username")
    void whenUserDeleteByUsernameAndUsernameExists_thenReturnNothing() {
        Mockito.doNothing().when(userRepository).deleteById(Mockito.any(Long.class));
        Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(mockUser);
        adminService.deleteUserByUserName(mockUser.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.any(Long.class));
        Mockito.verify(userRepository, Mockito.times(1)).findUserByUsername(Mockito.anyString());
    }

    @Test
    @DisplayName("Delete user by userId")
    void whenUserDeleteByIdAndIdExists_thenReturnNothing() {
        Mockito.doNothing().when(userRepository).deleteById(Mockito.any(Long.class));
        Mockito.when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(mockUser));
        adminService.deleteUserByUserId(mockUser.getId());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.any(Long.class));
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    @DisplayName("Save user with a existing name")
    void whenUserSaveAndUsernameIsTaken_thenExceptionIsThrown() {
        Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(mockUser);
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(null);
        try {
            adminService.saveUser(UserTransformer.transform(mockUser));
        } catch (Exception e) {
            assertTrue(e instanceof BadRequestException);
            assertEquals(e.getMessage(), "This user already exists!");
        }
        Mockito.verify(userRepository, Mockito.times(1)).findUserByUsername(Mockito.anyString());
    }

    @Test
    @DisplayName("Delete user by id when id does not exists")
    void whenUserDeleteByIdAndIdDoesNotExists_thenExceptionIsThrown() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        Mockito.doNothing().when(userRepository).deleteById(Mockito.any(Long.class));
        try {
            adminService.deleteUserByUserId(mockUser.getId());
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertEquals(e.getMessage(), "Cannot find user with id: " + mockUser.getId());
        }
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    @DisplayName("Delete user by username when username does not exists")
    void whenUserDeleteByUsernameAndUsernameDoesNotExists_thenExceptionIsThrown() {
        Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(null);
        Mockito.doNothing().when(userRepository).deleteById(Mockito.any(Long.class));
        try {
            adminService.deleteUserByUserName(mockUser.getUsername());
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertEquals(e.getMessage(), "Cannot find user with username: " + mockUser.getUsername());
        }
        Mockito.verify(userRepository, Mockito.times(1)).findUserByUsername(Mockito.anyString());
    }
}