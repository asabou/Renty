package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.dao.repository.IRoleRepository;
import com.mydegree.renty.service.impl.factories.UserFactory;
import com.mydegree.renty.service.model.RoleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class RoleServiceImplTest extends AbstractTest {
    private RoleServiceImpl roleService;

    @Mock
    private IRoleRepository roleRepository;

    private Iterable<RoleEntity> mockRoles;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
        final UserFactory userFactory = new UserFactory();
        mockRoles = userFactory.createMockAdminRoles();
    }

    @Test
    void whenFindAllRoles_thenReturnMockRoles() {
        when(roleRepository.findAll()).thenReturn(mockRoles);
        final List<RoleDTO> roles = roleService.findAllRoles();
        verify(roleRepository, times(1)).findAll();
        assertNotNull(roles);
        assertEquals(roles.size(), 3);
        assertEquals(roles.get(0).getRole(), "admin");
    }
}