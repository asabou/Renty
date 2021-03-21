package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceEntity;
import com.mydegree.renty.dao.entity.EntertainmentActivityPlaceId;
import com.mydegree.renty.dao.entity.ReservationEntity;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.impl.factories.EntertainmentActivityFactory;
import com.mydegree.renty.service.impl.factories.EntertainmentPlaceFactory;
import com.mydegree.renty.service.impl.factories.ReservationFactory;
import com.mydegree.renty.service.impl.factories.UserFactory;
import com.mydegree.renty.service.model.EntertainmentActivityPlaceIdDTO;
import com.mydegree.renty.service.model.ReservationInputDTO;
import com.mydegree.renty.service.model.ReservationOutputDTO;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@ActiveProfiles("test")
class ReservationServiceImplTest extends AbstractTest {
    @InjectMocks
    private ReservationServiceImpl reservationService;

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

    @Mock
    private IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository;

    private EntertainmentActivityPlaceEntity mockEntertainmentActivityPlaceEntity;
    private UserDetailsEntity mockUserDetails;
    private ReservationInputDTO mockReservationInputDTO;
    private ReservationEntity mockReservationEntity;
    private List<ReservationEntity> mockReservationEntities;
    private EntertainmentActivityFactory entertainmentActivityFactory;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationServiceImpl(userRepository, userDetailsRepository, roleRepository, entertainmentActivityRepository, passwordEncoder, reservationRepository, entertainmentActivityPlaceRepository);
        EntertainmentPlaceFactory entertainmentPlaceFactory = new EntertainmentPlaceFactory();
        mockEntertainmentActivityPlaceEntity = entertainmentPlaceFactory.createMockEntertainmentActivityPlaceEntity("activity", "place");
        UserFactory userFactory = new UserFactory();
        mockUserDetails = userFactory.createSimpleMockUserDetailsEntity("name");
        ReservationFactory reservationFactory = new ReservationFactory();
        mockReservationInputDTO = reservationFactory.createMockReservationInputDTO();
        mockReservationEntity = reservationFactory.createSimpleMockReservationEntity();
        mockReservationEntities = reservationFactory.createMockReservationEntities();
        entertainmentActivityFactory = new EntertainmentActivityFactory();
    }

    @Test
    void whenSaveReservationAndReservationDoesNotExistsAndPlaceAndActivityExistsAndUserExists_thenReturnNothing() {
        when(entertainmentActivityPlaceRepository.findById(any(EntertainmentActivityPlaceId.class))).thenReturn(Optional.of(mockEntertainmentActivityPlaceEntity));
        when(reservationRepository.findReservationEntityByEntertainmentActivityPlaceAndReservationDateAndReservationHour(any(EntertainmentActivityPlaceEntity.class), any(), anyInt())).thenReturn(null);
        when(userDetailsRepository.findById(anyLong())).thenReturn(Optional.of(mockUserDetails));
        when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(null); //we dont care for the result
        reservationService.saveReservation(mockReservationInputDTO);
        verify(entertainmentActivityPlaceRepository, times(1)).findById(any(EntertainmentActivityPlaceId.class));
        verify(reservationRepository, times(1)).findReservationEntityByEntertainmentActivityPlaceAndReservationDateAndReservationHour(any(EntertainmentActivityPlaceEntity.class), any(), anyInt());
        verify(userDetailsRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).save(any(ReservationEntity.class));
    }

    @Test
    void whenSaveReservationAndReservationDoesNotExistsAndEntertainmentActivityPlaceDoesNotExists_thenExceptionIsThrown() {
        when(entertainmentActivityPlaceRepository.findById(any(EntertainmentActivityPlaceId.class))).thenReturn(Optional.empty());
        when(reservationRepository.findReservationEntityByEntertainmentActivityPlaceAndReservationDateAndReservationHour(any(EntertainmentActivityPlaceEntity.class), any(), anyInt())).thenReturn(null);
        when(userDetailsRepository.findById(anyLong())).thenReturn(Optional.of(mockUserDetails));
        when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(null); //we dont care for the result
        try {
            reservationService.saveReservation(mockReservationInputDTO);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentActivityPlaceRepository, times(1)).findById(any(EntertainmentActivityPlaceId.class));
        verify(reservationRepository, times(0)).findReservationEntityByEntertainmentActivityPlaceAndReservationDateAndReservationHour(any(EntertainmentActivityPlaceEntity.class), any(), anyInt());
        verify(userDetailsRepository, times(0)).findById(anyLong());
        verify(reservationRepository, times(0)).save(any(ReservationEntity.class));
    }

    @Test
    void whenCancelReservationAndReservationExists_thenReturnNothing() {
        when(reservationRepository.findById(any())).thenReturn(Optional.of(mockReservationEntity));
        doNothing().when(reservationRepository).delete(any(ReservationEntity.class));
        reservationService.cancelReservation(1L);
        verify(reservationRepository, times(1)).findById(any());
        verify(reservationRepository, times(1)).delete(any(ReservationEntity.class));
    }

    @Test
    void whenFindAllActiveReservationsFromRenter_thenReturnMockReservationEntities() {
        when(reservationRepository.findReservationEntitiesByUserDetailsIdAndReservationDateIsGreaterThanEqualOrderByReservationDateAsc(anyLong(),
                any())).thenReturn(mockReservationEntities);
        final List<ReservationOutputDTO> reservations = reservationService.findAllActiveReservationsFromRenter(1L);
        assertNotNull(reservations);
        assertEquals(reservations.size(), 1);
        verify(reservationRepository, times(1)).findReservationEntitiesByUserDetailsIdAndReservationDateIsGreaterThanEqualOrderByReservationDateAsc(anyLong(),
                any());
    }

    @Test
    void whenFindAllReservations_thenReturnMockReservationEntities() {
        when(reservationRepository.findAll()).thenReturn(mockReservationEntities);
        final List<ReservationOutputDTO> reservations = reservationService.findAllReservations();
        assertNotNull(reservations);
        assertEquals(reservations.size(), 1);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void whenFindAllActiveReservations_thenReturnMockReservationEntities() {
        when(reservationRepository.findReservationEntitiesByReservationDateGreaterThan(any())).thenReturn(mockReservationEntities);
        final List<ReservationOutputDTO> reservations = reservationService.findAllActiveReservations();
        assertNotNull(reservations);
        assertEquals(reservations.size(), 1);
        verify(reservationRepository, times(1)).findReservationEntitiesByReservationDateGreaterThan(any());
    }

    @Test
    void whenFindAllActiveReservationsByEntertainmentActivityPlaceAndEntertainmentActivityPlaceExists_thenReturnMockReservationEntities() {
        when(entertainmentActivityPlaceRepository.findById(any())).thenReturn(Optional.of(mockEntertainmentActivityPlaceEntity));
        when(reservationRepository.findReservationEntitiesByEntertainmentActivityPlaceAndReservationDateIsGreaterThanEqual(any(EntertainmentActivityPlaceEntity.class),
                any())).thenReturn(mockReservationEntities);
        final EntertainmentActivityPlaceIdDTO id = entertainmentActivityFactory.createMockEntertainmentActivityPlaceIdDTO();
        final List<ReservationOutputDTO> reservations = reservationService.findAllActiveReservationsByEntertainmentActivityPlace(id);
        assertNotNull(reservations);
        assertEquals(reservations.size(), 1);
        verify(entertainmentActivityPlaceRepository, times(1)).findById(any());
        verify(reservationRepository, times(1)).findReservationEntitiesByEntertainmentActivityPlaceAndReservationDateIsGreaterThanEqual(any(EntertainmentActivityPlaceEntity.class), any());
    }

    @Test
    void whenFindAllActiveReservationsByEntertainmentActivityPlaceAndEntertainmentActivityPlaceDoesNotExists_thenReturnMockReservationEntities() {
        when(entertainmentActivityPlaceRepository.findById(any())).thenReturn(Optional.empty());
        when(reservationRepository.findReservationEntitiesByEntertainmentActivityPlaceAndReservationDateIsGreaterThanEqual(any(EntertainmentActivityPlaceEntity.class),
                any())).thenReturn(mockReservationEntities);
        final EntertainmentActivityPlaceIdDTO id = entertainmentActivityFactory.createMockEntertainmentActivityPlaceIdDTO();
        try {
            final List<ReservationOutputDTO> reservations = reservationService.findAllActiveReservationsByEntertainmentActivityPlace(id);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentActivityPlaceRepository, times(1)).findById(any());
        verify(reservationRepository, times(0)).findReservationEntitiesByEntertainmentActivityPlaceAndReservationDateIsGreaterThanEqual(any(EntertainmentActivityPlaceEntity.class),
                any());
    }

    @Test
    void whenFindAllActiveReservationsFromAnOwner_thenReturnMockReservationEntities() {
        when(reservationRepository.findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlace_UserDetailsIdAndReservationDateIsGreaterThanEqual(anyLong(),
                any())).thenReturn(mockReservationEntities);
        final List<ReservationOutputDTO> reservations = reservationService.findAllActiveReservationsFromAnOwner(1L);
        assertNotNull(reservations);
        assertEquals(reservations.size(), 1);
        verify(reservationRepository, times(1)).findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlace_UserDetailsIdAndReservationDateIsGreaterThanEqual(anyLong(), any());
    }

    @Test
    void whenFindAllActiveReservationsByActivityAndPlaceAndEntertainmentActivityPlaceExists_thenReturnMockReservationEntities() {
        when(entertainmentActivityPlaceRepository.findById(any())).thenReturn(Optional.of(mockEntertainmentActivityPlaceEntity));
        when(reservationRepository.findReservationEntitiesByEntertainmentActivityPlaceAndReservationDateIsGreaterThanEqual(any(EntertainmentActivityPlaceEntity.class),
                any())).thenReturn(mockReservationEntities);
        final EntertainmentActivityPlaceIdDTO id = entertainmentActivityFactory.createMockEntertainmentActivityPlaceIdDTO();
        final List<ReservationInputDTO> reservations = reservationService.findAllActiveReservationsByActivityAndPlace(id);
        assertNotNull(reservations);
        assertEquals(reservations.size(), 1);
        verify(entertainmentActivityPlaceRepository, times(1)).findById(any());
        verify(reservationRepository, times(1)).findReservationEntitiesByEntertainmentActivityPlaceAndReservationDateIsGreaterThanEqual(any(EntertainmentActivityPlaceEntity.class), any());
    }

    @Test
    void whenFindAllActiveReservationsByActivityAndPlaceAndEntertainmentActivityPlaceDoesNotExists_thenReturnMockReservationEntities() {
        when(entertainmentActivityPlaceRepository.findById(any())).thenReturn(Optional.empty());
        when(reservationRepository.findReservationEntitiesByEntertainmentActivityPlaceAndReservationDateIsGreaterThanEqual(any(EntertainmentActivityPlaceEntity.class),
                any())).thenReturn(mockReservationEntities);
        final EntertainmentActivityPlaceIdDTO id = entertainmentActivityFactory.createMockEntertainmentActivityPlaceIdDTO();
        try {
            final List<ReservationInputDTO> reservations = reservationService.findAllActiveReservationsByActivityAndPlace(id);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentActivityPlaceRepository, times(1)).findById(any());
        verify(reservationRepository, times(0)).findReservationEntitiesByEntertainmentActivityPlaceAndReservationDateIsGreaterThanEqual(any(EntertainmentActivityPlaceEntity.class), any());
    }
}