package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.entity.custom.CustomEntertainmentActivity;
import com.mydegree.renty.dao.entity.custom.CustomEntertainmentPlace;
import com.mydegree.renty.dao.entity.custom.CustomReservationDate;
import com.mydegree.renty.dao.entity.custom.CustomReservationHour;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.impl.factories.EntertainmentActivityFactory;
import com.mydegree.renty.service.impl.factories.EntertainmentPlaceFactory;
import com.mydegree.renty.service.impl.factories.ReservationFactory;
import com.mydegree.renty.service.impl.factories.UserFactory;
import com.mydegree.renty.service.model.CustomEntertainmentActivityDTO;
import com.mydegree.renty.service.model.CustomEntertainmentPlaceDTO;
import com.mydegree.renty.service.model.CustomReservationDateDTO;
import com.mydegree.renty.service.model.CustomReservationHourDTO;
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
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class StatisticsServiceImplTest extends AbstractTest {
    @InjectMocks
    private StatisticsServiceImpl statisticsService;

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
    private IEntertainmentPlaceRepository entertainmentPlaceRepository;

    private UserFactory userFactory;
    private UserEntity mockUserAdmin;
    private UserEntity mockUserOwner;
    private UserEntity mockUserRenter;
    private EntertainmentActivityFactory entertainmentActivityFactory;
    private Iterable<CustomEntertainmentActivity> mockCustomEntertainmentActivities;
    private EntertainmentPlaceFactory entertainmentPlaceFactory;
    private Iterable<CustomEntertainmentPlace> mockCustomEntertainmentPlaces;
    private ReservationFactory reservationFactory;
    private Iterable<CustomReservationHour> mockCustomReservationHours;
    private Iterable<CustomReservationDate> mockCustomReservationDates;

    @BeforeEach
    void setUp() {
        statisticsService = new StatisticsServiceImpl(userRepository,
                userDetailsRepository,
                roleRepository,
                entertainmentActivityRepository,
                passwordEncoder,
                reservationRepository,
                entertainmentPlaceRepository);
        userFactory = new UserFactory();
        mockUserAdmin = userFactory.createMockUserAdmin("admin");
        mockUserOwner = userFactory.createMockUserOwner("owner");
        mockUserRenter = userFactory.createMockUserRenter("renter");
        entertainmentActivityFactory = new EntertainmentActivityFactory();
        mockCustomEntertainmentActivities = entertainmentActivityFactory.createMockCustomEntertainmentActivities();
        entertainmentPlaceFactory = new EntertainmentPlaceFactory();
        mockCustomEntertainmentPlaces = entertainmentPlaceFactory.createMockCustomEntertainmentPlaces();
        reservationFactory = new ReservationFactory();
        mockCustomReservationHours = reservationFactory.createMockCustomReservationHours();
        mockCustomReservationDates = reservationFactory.createMockCustomReservationDates();
    }

    @Test
    void whenFindTopMostRentedEntertainmentActivitiesAndUserExistsAndUserIsAdmin_thenReturnCustomEntertainmentActivityDTO() {
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUserAdmin));
        when(entertainmentActivityRepository.findTopMostRentedEntertainmentActivitiesForAdminBetweenDates(any(), any())).thenReturn(mockCustomEntertainmentActivities);
        final List<CustomEntertainmentActivityDTO> activities = statisticsService.findTopMostRentedEntertainmentActivities(1L, null, null);
        assertNotNull(activities);
        assertEquals(activities.size(), 1);
        verify(userRepository, times(2)).findById(any());
        verify(entertainmentActivityRepository, times(1)).findTopMostRentedEntertainmentActivitiesForAdminBetweenDates(any(), any());
    }

    @Test
    void whenFindTopMostRentedEntertainmentActivitiesAndUserDoesNotExistsAndUserAdmin_thenReturnCustomEntertainmentActivityDTO() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(entertainmentActivityRepository.findTopMostRentedEntertainmentActivitiesForAdminBetweenDates(any(), any())).thenReturn(mockCustomEntertainmentActivities);
        try {
            final List<CustomEntertainmentActivityDTO> activities = statisticsService.findTopMostRentedEntertainmentActivities(1L, null, null);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(userRepository, times(1)).findById(any());
        verify(entertainmentActivityRepository, times(0)).findTopMostRentedEntertainmentActivitiesForAdminBetweenDates(any(), any());
    }

    @Test
    void whenFindTopMostRentedEntertainmentActivitiesAndUserExistsAndUserIsOwner_thenReturnCustomEntertainmentActivityDTO() {
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUserOwner));
        when(entertainmentActivityRepository.findTopMostRentedEntertainmentActivitiesForOwnerBetweenDates(any(), any(), any())).thenReturn(mockCustomEntertainmentActivities);
        final List<CustomEntertainmentActivityDTO> activities = statisticsService.findTopMostRentedEntertainmentActivities(1L, null, null);
        assertNotNull(activities);
        assertEquals(activities.size(), 1);
        verify(userRepository, times(1)).findById(any());
        verify(entertainmentActivityRepository, times(1)).findTopMostRentedEntertainmentActivitiesForOwnerBetweenDates(any(), any(), any());
    }

    @Test
    void whenFindTopMostRentedEntertainmentPlacesAndUserExistsAndUserIsOwner_thenReturnMockCustomEntertainmentPlaces() {
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUserOwner));
        when(entertainmentPlaceRepository.findTopMostRentedEntertainmentPlacesForOwnerBetweenDates(any(), any(), any())).thenReturn(mockCustomEntertainmentPlaces);
        final List<CustomEntertainmentPlaceDTO> places = statisticsService.findTopMostRentedEntertainmentPlaces(1L, null, null);
        assertNotNull(places);
        assertEquals(places.size(), 1);
        verify(userRepository, times(1)).findById(any());
        verify(entertainmentPlaceRepository, times(1)).findTopMostRentedEntertainmentPlacesForOwnerBetweenDates(any(), any(), any());
    }

    @Test
    void whenFindTopMostRentedHourReservationsAndUserIsOwnerAndPlaceIsNotNull_thenReturnMockCustomReservationHour() {
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUserOwner));
        when(reservationRepository.findTopMostRentedHourForOwnerFromPlaceBetweenDates(any(), eq(1L), any(), any())).thenReturn(mockCustomReservationHours);
        final List<CustomReservationHourDTO> res = statisticsService.findTopMostRentedHourReservations(1L, 1L, null, null);
        assertNotNull(res);
        assertEquals(res.size(), 1);
        verify(userRepository, times(1)).findById(any());
        verify(reservationRepository, times(1)).findTopMostRentedHourForOwnerFromPlaceBetweenDates(any(), eq(1L), any(), any());
    }

    @Test
    void whenFindTopMostRentedHourReservationsAndUserIsOwnerAndPlaceIsNull_thenReturnMockCustomReservationHour() {
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUserOwner));
        when(reservationRepository.findTopMostRentedHourForOwnerBetweenDates(any(), any(), any())).thenReturn(mockCustomReservationHours);
        final List<CustomReservationHourDTO> res = statisticsService.findTopMostRentedHourReservations(1L, null, null, null);
        assertNotNull(res);
        assertEquals(res.size(), 1);
        verify(userRepository, times(1)).findById(any());
        verify(reservationRepository, times(1)).findTopMostRentedHourForOwnerBetweenDates(any(), any(), any());
    }

    @Test
    void whenFindTopMostRentedHourReservationsAndUserIsAdmin_thenReturnMockCustomReservationHour() {
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUserAdmin));
        when(reservationRepository.findTopMostRentedHourForAdminBetweenDates(any(), any())).thenReturn(mockCustomReservationHours);
        final List<CustomReservationHourDTO> res = statisticsService.findTopMostRentedHourReservations(1L, null, null, null);
        assertNotNull(res);
        assertEquals(res.size(), 1);
        verify(userRepository, times(2)).findById(any());
        verify(reservationRepository, times(1)).findTopMostRentedHourForAdminBetweenDates(any(), any());
    }

    @Test
    void whenFindTopMostRentedDateReservationsAndUserIsOwnerAndPlaceIsNull_thenReturnMockCustomReservationDates() {
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUserOwner));
        when(reservationRepository.findTopMostRentedDatesForOwner(any())).thenReturn(mockCustomReservationDates);
        final List<CustomReservationDateDTO> dates = statisticsService.findTopMostRentedDateReservations(1L, null);
        assertNotNull(dates);
        assertEquals(dates.size(), 1);
        verify(userRepository, times(1)).findById(any());
        verify(reservationRepository, times(1)).findTopMostRentedDatesForOwner(any());
    }

    @Test
    void whenFindTopMostRentedDateReservationsAndUserIsOwnerAndPlaceIsNotNull_thenReturnMockCustomReservationDates() {
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUserOwner));
        when(reservationRepository.findTopMostRentedDatesForOwnerFromPlace(any(), eq(1L))).thenReturn(mockCustomReservationDates);
        final List<CustomReservationDateDTO> dates = statisticsService.findTopMostRentedDateReservations(1L, 1L);
        assertNotNull(dates);
        assertEquals(dates.size(), 1);
        verify(userRepository, times(1)).findById(any());
        verify(reservationRepository, times(1)).findTopMostRentedDatesForOwnerFromPlace(any(), eq(1L));
    }

    @Test
    void whenFindTopMostRentedDateReservationsAndUserIsAdmin_thenReturnMockCustomReservationDates() {
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUserAdmin));
        when(reservationRepository.findTopMostRentedDatesForAdmin()).thenReturn(mockCustomReservationDates);
        final List<CustomReservationDateDTO> dates = statisticsService.findTopMostRentedDateReservations(1L, 1L);
        assertNotNull(dates);
        assertEquals(dates.size(), 1);
        verify(userRepository, times(2)).findById(any());
        verify(reservationRepository, times(1)).findTopMostRentedDatesForAdmin();
    }


}