package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.*;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.exceptions.BadRequestException;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.impl.factories.EntertainmentActivityFactory;
import com.mydegree.renty.service.impl.factories.EntertainmentPlaceFactory;
import com.mydegree.renty.service.impl.factories.ReservationFactory;
import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import com.mydegree.renty.service.model.EntertainmentActivityInputDTO;
import com.mydegree.renty.service.model.EntertainmentActivityOutputDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class EntertainmentActivityServiceImplTest extends AbstractTest {
    private EntertainmentActivityServiceImpl entertainmentActivityService;

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

    @Mock
    private IEntertainmentPlaceRepository entertainmentPlaceRepository;

    @Mock
    private IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository;

    private Iterable<EntertainmentActivityEntity> mockEntertainmentActivityEntities;
    private EntertainmentActivityEntity mockEntertainmentActivityEntity;
    private EntertainmentActivityFactory entertainmentActivityFactory;
    private Iterable<EntertainmentActivityPlaceEntity> mockEntertainmentActivityPlaceEntities;
    private EntertainmentActivityPlaceEntity mockEntertainmentActivityPlaceEntity;
    private Iterable<ReservationEntity> mockReservationEntities;
    private EntertainmentPlaceEntity mockEntertainmentPlaceEntity;

    @BeforeEach
    void setUp() {
        entertainmentActivityService = new EntertainmentActivityServiceImpl(userRepository, userDetailsRepository, roleRepository,
                entertainmentActivityRepository, passwordEncoder, entertainmentActivityPlaceRepository, reservationRepository,
                entertainmentPlaceRepository);
        entertainmentActivityFactory = new EntertainmentActivityFactory();
        EntertainmentPlaceFactory entertainmentPlaceFactory = new EntertainmentPlaceFactory();
        mockEntertainmentActivityEntities = entertainmentActivityFactory.createMockEntertainmentActivities();
        mockEntertainmentActivityEntity = entertainmentActivityFactory.createSimpleMockEntertainmentActivity("football");
        mockEntertainmentActivityPlaceEntities = entertainmentPlaceFactory.createMockEntertainmentActivityPlaceEntities();
        mockEntertainmentActivityPlaceEntity = entertainmentPlaceFactory.createMockEntertainmentActivityPlaceEntity("football", "place");
        ReservationFactory reservationFactory = new ReservationFactory();
        mockReservationEntities = reservationFactory.createMockReservationEntities();
        mockEntertainmentPlaceEntity = entertainmentPlaceFactory.createSimpleMockEntertainmentPlaceEntity("place");
    }

    @Test
    void whenFindEntertainmentActivitiesByPlaceId_thenReturnMockEntertainmentActivityEntities() {
        when(entertainmentActivityRepository.findEntertainmentActivityEntitiesByEntertainmentPlaceId(anyLong())).thenReturn(mockEntertainmentActivityEntities);
        final List<EntertainmentActivityDTO> activities = entertainmentActivityService.findEntertainmentActivitiesByEntertainmentPlaceId(1L);
        assertNotNull(activities);
        assertEquals(activities.size(), 3);
        assertEquals(activities.get(0).getName(), "football");
        verify(entertainmentActivityRepository, times(1)).findEntertainmentActivityEntitiesByEntertainmentPlaceId(1L);
    }

    @Test
    void whenSaveEntertainmentActivityEntityAndItDoesNotExists_thenReturnNothing() {
        final EntertainmentActivityDTO entertainmentActivityDTO = entertainmentActivityFactory.createSimpleMockEntertainmentActivityDTO("football");
        when(entertainmentActivityRepository.findEntertainmentActivityEntityByName(any())).thenReturn(null);
        when(entertainmentActivityRepository.save(any())).thenReturn(null); //we don't care for the result
        entertainmentActivityService.saveEntertainmentActivity(entertainmentActivityDTO);
        verify(entertainmentActivityRepository, times(1)).findEntertainmentActivityEntityByName("football");
        verify(entertainmentActivityRepository, times(1)).save(any());
    }

    @Test
    void whenSaveEntertainmentActivityEntityAndItAlreadyExists_thenExceptionIsThrown() {
        final EntertainmentActivityDTO entertainmentActivityDTO = entertainmentActivityFactory.createSimpleMockEntertainmentActivityDTO("football");
        when(entertainmentActivityRepository.findEntertainmentActivityEntityByName(anyString())).thenReturn(mockEntertainmentActivityEntity);
        when(entertainmentActivityRepository.save(any(EntertainmentActivityEntity.class))).thenReturn(null); //we don't care for the result
        try {
            entertainmentActivityService.saveEntertainmentActivity(entertainmentActivityDTO);
        } catch (Exception e) {
            assertTrue(e instanceof BadRequestException);
            assertTrue(e.getMessage().contains("already exists"));
        }
        verify(entertainmentActivityRepository, times(1)).findEntertainmentActivityEntityByName(eq("football"));
        verify(entertainmentActivityRepository, times(0)).save(mockEntertainmentActivityEntity);
    }

    @Test
    void whenFindEntertainmentActivitiesDetailsByEntertainmentPlaceId_thenReturnMockEntertainmentActivityPlaceEntities() {
        when(entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntitiesByEntertainmentPlace_Id(anyLong())).thenReturn(mockEntertainmentActivityPlaceEntities);
        final List<EntertainmentActivityOutputDTO> results =
                entertainmentActivityService.findEntertainmentActivitiesDetailsByEntertainmentPlaceId(1L);
        assertNotNull(results);
        assertEquals(results.size(), 1);
        assertEquals(results.get(0).getEntertainmentActivityName(), "football");
        assertEquals(results.get(0).getEntertainmentActivityPrice(), 40.0);
        assertEquals(results.get(0).getMaxPeopleAllowed(), 12);
        verify(entertainmentActivityPlaceRepository, times(1)).findEntertainmentActivityPlaceEntitiesByEntertainmentPlace_Id(anyLong());
    }

    @Test
    void whenFindEntertainmentActivityByIdAndItExists_thenReturnMockEntertainmentActivity() {
        when(entertainmentActivityRepository.findById(anyLong())).thenReturn(Optional.of(mockEntertainmentActivityEntity));
        final EntertainmentActivityDTO activityDTO = entertainmentActivityService.findEntertainmentActivityById(1L);
        assertNotNull(activityDTO);
        assertTrue(activityDTO.getName().contains("football"));
        verify(entertainmentActivityRepository, times(1)).findById(anyLong());
    }

    @Test
    void whenFindEntertainmentActivityByIdAntItDoesNotExists_thenExceptionIsThrown() {
        when(entertainmentActivityRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            final EntertainmentActivityDTO activityDTO = entertainmentActivityService.findEntertainmentActivityById(1L);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentActivityRepository, times(1)).findById(anyLong());
    }

    @Test
    void whenUpdateEntertainmentActivityForPlaceAndItExists_thenReturnNothing() {
        when(entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(),
                anyLong())).thenReturn(mockEntertainmentActivityPlaceEntity);
        when(entertainmentActivityPlaceRepository.save(any(EntertainmentActivityPlaceEntity.class))).thenReturn(null); //we dont care for the result
        final EntertainmentActivityInputDTO mockActivityInput = entertainmentActivityFactory.createMockEntertainmentActivityInputDTO();
        entertainmentActivityService.updateEntertainmentActivityForPlace(mockActivityInput);
        assertNotNull(mockActivityInput);
        assertEquals(mockActivityInput.getEntertainmentActivityId(), 1L);
        assertEquals(mockActivityInput.getEntertainmentPlaceId(), 1L);
        assertEquals(mockActivityInput.getMaxPeopleAllowed(), 12);
        assertEquals(mockActivityInput.getPrice(), 40);
        verify(entertainmentActivityPlaceRepository, times(1)).findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(), anyLong());
        verify(entertainmentActivityPlaceRepository, times(1)).save(any(EntertainmentActivityPlaceEntity.class));
    }

    @Test
    void whenUpdateEntertainmentActivityForPlaceAndItDoesNotExists_thenExceptionIsThrown() {
        when(entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(),
                anyLong())).thenReturn(null);
        when(entertainmentActivityPlaceRepository.save(any(EntertainmentActivityPlaceEntity.class))).thenReturn(null); //we dont care for the result
        final EntertainmentActivityInputDTO mockActivityInput = entertainmentActivityFactory.createMockEntertainmentActivityInputDTO();
        try {
            entertainmentActivityService.updateEntertainmentActivityForPlace(mockActivityInput);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentActivityPlaceRepository, times(1)).findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(), anyLong());
        verify(entertainmentActivityPlaceRepository, times(0)).save(any(EntertainmentActivityPlaceEntity.class));
    }

    @Test
    void whenFindEntertainmentActivityForPlaceAndItExists_thenReturnEntertainmentActivityInputDTO() {
        when(entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(),
                anyLong())).thenReturn(mockEntertainmentActivityPlaceEntity);
        final EntertainmentActivityInputDTO mockActivityInput = entertainmentActivityFactory.createMockEntertainmentActivityInputDTO();
        final EntertainmentActivityInputDTO activityInputDTO =
                entertainmentActivityService.findEntertainmentActivityForPlace(mockActivityInput);
        assertNotNull(activityInputDTO);
        assertEquals(activityInputDTO.getEntertainmentActivityId(), 1L);
        assertEquals(activityInputDTO.getEntertainmentPlaceId(), 1L);
        assertEquals(activityInputDTO.getPrice(), 40.0);
        assertEquals(activityInputDTO.getMaxPeopleAllowed(), 12);
        verify(entertainmentActivityPlaceRepository, times(1)).findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(), anyLong());
    }

    @Test
    void whenFindEntertainmentActivityForPlaceAndItDoesNotExists_thenExceptionIsThrown() {
        when(entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(),
                anyLong())).thenReturn(null);
        final EntertainmentActivityInputDTO mockActivityInput = entertainmentActivityFactory.createMockEntertainmentActivityInputDTO();
        try {
            final EntertainmentActivityInputDTO activityInputDTO =
                    entertainmentActivityService.findEntertainmentActivityForPlace(mockActivityInput);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentActivityPlaceRepository, times(1)).findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(), anyLong());
    }

    @Test
    void whenDeleteEntertainmentActivityForPlaceAndItExists_thenReturnNothing() {
        when(entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(),
                anyLong())).thenReturn(mockEntertainmentActivityPlaceEntity);
        when(reservationRepository.findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceIdAndEntertainmentActivityPlace_EntertainmentActivityId(anyLong(), anyLong())).thenReturn(mockReservationEntities);
        doNothing().when(reservationRepository).deleteAll(anyCollection());
        doNothing().when(entertainmentActivityPlaceRepository).delete(any(EntertainmentActivityPlaceEntity.class));
        doNothing().when(entertainmentActivityPlaceRepository).delete(any(EntertainmentActivityPlaceEntity.class));
        final EntertainmentActivityInputDTO mockActivityDTO = entertainmentActivityFactory.createMockEntertainmentActivityInputDTO();
        entertainmentActivityService.deleteEntertainmentActivityForPlace(mockActivityDTO);
        verify(entertainmentActivityPlaceRepository, times(2)).findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(), anyLong());
        verify(reservationRepository, times(1)).
                findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceIdAndEntertainmentActivityPlace_EntertainmentActivityId(anyLong(), anyLong());
        verify(reservationRepository, times(1)).deleteAll(anyCollection());
        verify(entertainmentActivityPlaceRepository, times(2)).delete(any(EntertainmentActivityPlaceEntity.class));
    }

    @Test
    void whenDeleteEntertainmentActivityForPlaceAndItDoesNotExists_thenExceptionIsThrown() {
        when(entertainmentActivityPlaceRepository.findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(),
                anyLong())).thenReturn(null);
        when(reservationRepository.findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceIdAndEntertainmentActivityPlace_EntertainmentActivityId(anyLong(), anyLong())).thenReturn(mockReservationEntities);
        doNothing().when(reservationRepository).deleteAll(anyCollection());
        doNothing().when(entertainmentActivityPlaceRepository).delete(any(EntertainmentActivityPlaceEntity.class));
        doNothing().when(entertainmentActivityPlaceRepository).delete(any(EntertainmentActivityPlaceEntity.class));
        final EntertainmentActivityInputDTO mockActivityDTO = entertainmentActivityFactory.createMockEntertainmentActivityInputDTO();
        try {
            entertainmentActivityService.deleteEntertainmentActivityForPlace(mockActivityDTO);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentActivityPlaceRepository, times(1)).findEntertainmentActivityPlaceEntityByEntertainmentActivityIdAndEntertainmentPlaceId(anyLong(), anyLong());
        verify(reservationRepository, times(0)).
                findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceIdAndEntertainmentActivityPlace_EntertainmentActivityId(anyLong(), anyLong());
        verify(reservationRepository, times(0)).deleteAll(anyCollection());
        verify(entertainmentActivityPlaceRepository, times(0)).delete(any(EntertainmentActivityPlaceEntity.class));
    }

    @Test
    void whenFindAll_thenReturnMockEntertainmentActivities() {
        when(entertainmentActivityRepository.findAll()).thenReturn(mockEntertainmentActivityEntities);
        final List<EntertainmentActivityDTO> entertainmentActivities = entertainmentActivityService.findAll();
        assertNotNull(entertainmentActivities);
        assertEquals(entertainmentActivities.size(), 3);
        verify(entertainmentActivityRepository, times(1)).findAll();
    }

    @Test
    void whenSaveEntertainmentActivityForPlaceAndPlaceExistsAndActivityExists_thenReturnNothing() {
        when(entertainmentActivityPlaceRepository.findById(any(EntertainmentActivityPlaceId.class))).thenReturn(Optional.empty());
        when(entertainmentPlaceRepository.findById(anyLong())).thenReturn(Optional.of(mockEntertainmentPlaceEntity));
        when(entertainmentActivityRepository.findById(anyLong())).thenReturn(Optional.of(mockEntertainmentActivityEntity));
        when(entertainmentActivityPlaceRepository.save(any(EntertainmentActivityPlaceEntity.class))).thenReturn(null); //we dont care for the result
        final EntertainmentActivityInputDTO activityInputDTO = entertainmentActivityFactory.createMockEntertainmentActivityInputDTO();
        entertainmentActivityService.saveEntertainmentActivityForPlace(activityInputDTO);
        verify(entertainmentActivityPlaceRepository, times(1)).findById(any(EntertainmentActivityPlaceId.class));
        verify(entertainmentPlaceRepository, times(1)).findById(anyLong());
        verify(entertainmentActivityRepository, times(1)).findById(anyLong());
        verify(entertainmentActivityPlaceRepository, times(1)).save(any(EntertainmentActivityPlaceEntity.class));
    }

    @Test
    void whenSaveEntertainmentActivityForPlaceAndActivityAlreadyExists_thenExceptionIsThrown() {
        when(entertainmentActivityPlaceRepository.findById(any(EntertainmentActivityPlaceId.class))).thenReturn(Optional.of(mockEntertainmentActivityPlaceEntity));
        when(entertainmentPlaceRepository.findById(anyLong())).thenReturn(Optional.of(mockEntertainmentPlaceEntity));
        when(entertainmentActivityRepository.findById(anyLong())).thenReturn(Optional.of(mockEntertainmentActivityEntity));
        when(entertainmentActivityPlaceRepository.save(any(EntertainmentActivityPlaceEntity.class))).thenReturn(null); //we dont care for the result
        final EntertainmentActivityInputDTO activityInputDTO = entertainmentActivityFactory.createMockEntertainmentActivityInputDTO();
        try {
            entertainmentActivityService.saveEntertainmentActivityForPlace(activityInputDTO);
        } catch (Exception e) {
            assertTrue(e instanceof BadRequestException);
            assertTrue(e.getMessage().contains("already exists"));
        }
        verify(entertainmentActivityPlaceRepository, times(1)).findById(any(EntertainmentActivityPlaceId.class));
        verify(entertainmentPlaceRepository, times(0)).findById(anyLong());
        verify(entertainmentActivityRepository, times(0)).findById(anyLong());
        verify(entertainmentActivityPlaceRepository, times(0)).save(any(EntertainmentActivityPlaceEntity.class));
    }

    @Test
    void whenSaveEntertainmentActivityForPlaceAndActivityDoesNotExistsAndPlaceOrActivityDoesNotExists_thenExceptionIsThrown() {
        when(entertainmentActivityPlaceRepository.findById(any(EntertainmentActivityPlaceId.class))).thenReturn(Optional.empty());
        when(entertainmentPlaceRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(entertainmentActivityRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(entertainmentActivityPlaceRepository.save(any(EntertainmentActivityPlaceEntity.class))).thenReturn(null); //we dont care for the result
        final EntertainmentActivityInputDTO activityInputDTO = entertainmentActivityFactory.createMockEntertainmentActivityInputDTO();
        try {
            entertainmentActivityService.saveEntertainmentActivityForPlace(activityInputDTO);
        } catch (Exception e) {
            assertTrue(e instanceof BadRequestException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentActivityPlaceRepository, times(1)).findById(any(EntertainmentActivityPlaceId.class));
        verify(entertainmentPlaceRepository, times(1)).findById(anyLong());
        verify(entertainmentActivityRepository, times(1)).findById(anyLong());
        verify(entertainmentActivityPlaceRepository, times(0)).save(any(EntertainmentActivityPlaceEntity.class));
    }
}