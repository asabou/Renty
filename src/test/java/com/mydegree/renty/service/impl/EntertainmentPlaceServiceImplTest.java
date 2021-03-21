package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.EntertainmentActivityEntity;
import com.mydegree.renty.dao.entity.EntertainmentPlaceEntity;
import com.mydegree.renty.dao.entity.ReservationEntity;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.impl.factories.EntertainmentActivityFactory;
import com.mydegree.renty.service.impl.factories.EntertainmentPlaceFactory;
import com.mydegree.renty.service.impl.factories.ReservationFactory;
import com.mydegree.renty.service.impl.factories.UserFactory;
import com.mydegree.renty.service.model.EntertainmentPlaceDTO;
import com.mydegree.renty.service.model.EntertainmentPlaceInputDTO;
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
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class EntertainmentPlaceServiceImplTest extends AbstractTest {
    @InjectMocks
    private EntertainmentPlaceServiceImpl entertainmentPlaceService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IEntertainmentPlaceRepository entertainmentPlaceRepository;

    @Mock
    private IUserDetailsRepository userDetailsRepository;

    @Mock
    private IEntertainmentActivityRepository entertainmentActivityRepository;

    @Mock
    private IAddressRepository addressRepository;

    @Mock
    private IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IReservationRepository reservationRepository;

    private EntertainmentPlaceFactory entertainmentPlaceFactory;
    private Iterable<EntertainmentPlaceEntity> mockEntertainmentPlaceEntities;
    private UserDetailsEntity mockUserDetailsEntity;
    private EntertainmentActivityEntity mockEntertainmentActivityEntity;
    private EntertainmentPlaceEntity mockEntertainmentPlaceEntity;
    private List<ReservationEntity> mockReservationEntities;

    @BeforeEach
    void setUp() {
        entertainmentPlaceService = new EntertainmentPlaceServiceImpl(userRepository, entertainmentPlaceRepository, userDetailsRepository, entertainmentActivityRepository, addressRepository,
                entertainmentActivityPlaceRepository, roleRepository, passwordEncoder, reservationRepository);
        entertainmentPlaceFactory = new EntertainmentPlaceFactory();
        mockEntertainmentPlaceEntities = entertainmentPlaceFactory.createMockEntertainmentPlaceEntities();
        final UserFactory userFactory = new UserFactory();
        mockUserDetailsEntity = userFactory.createSimpleMockUserDetailsEntity("name");
        final EntertainmentActivityFactory entertainmentActivityFactory = new EntertainmentActivityFactory();
        mockEntertainmentActivityEntity = entertainmentActivityFactory.createSimpleMockEntertainmentActivity("activity");
        mockEntertainmentPlaceEntity = entertainmentPlaceFactory.createSimpleMockEntertainmentPlaceEntity("place");
        final ReservationFactory reservationFactory = new ReservationFactory();
        mockReservationEntities = reservationFactory.createMockReservationEntities();
    }

    @Test
    void whenFindAllEntertainmentPlaceEntities_thenReturnMockEntertainmentPlaceEntities() {
        when(entertainmentPlaceRepository.findAll()).thenReturn(mockEntertainmentPlaceEntities);
        final List<EntertainmentPlaceDTO> places = entertainmentPlaceService.findAllEntertainmentPlaces();
        assertNotNull(places);
        assertEquals(places.size(), 1);
        verify(entertainmentPlaceRepository, times(1)).findAll();
    }

    @Test
    void whenFindAllOwnedEntertainmentPlaces_thenReturnMockEntertainmentPlaces() {
        when(entertainmentPlaceRepository.findEntertainmentPlaceEntitiesByUserDetailsId(anyLong())).thenReturn(mockEntertainmentPlaceEntities);
        final List<EntertainmentPlaceDTO> places = entertainmentPlaceService.findAllOwnedEntertainmentPlaces(1L);
        assertNotNull(places);
        assertEquals(places.size(), 1);
        verify(entertainmentPlaceRepository, times(1)).findEntertainmentPlaceEntitiesByUserDetailsId(anyLong());
    }

    @Test
    void whenSaveEntertainmentPlaceAndUserExistsAndEntertainmentActivityExists_thenReturnNothing() {
        when(userDetailsRepository.findById(any())).thenReturn(Optional.of(mockUserDetailsEntity));
        when(entertainmentActivityRepository.findEntertainmentActivityEntityByName(any())).thenReturn(mockEntertainmentActivityEntity);
        when(entertainmentActivityRepository.save(any(EntertainmentActivityEntity.class))).thenReturn(null);//we dont care for the result
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null); //we dont care for the result
        when(entertainmentPlaceRepository.save(any(EntertainmentPlaceEntity.class))).thenReturn(null); //we dont care for the result
        when(addressRepository.save(any())).thenReturn(null); //we dont care for the result
        when(entertainmentActivityPlaceRepository.saveAll(anyCollection())).thenReturn(null); //we dont care for the result
        final EntertainmentPlaceInputDTO place = entertainmentPlaceFactory.createMockEntertainmentPlaceInputDTO();
        entertainmentPlaceService.saveEntertainmentPlace(place);
        verify(userDetailsRepository, times(1)).findById(any());
        verify(entertainmentActivityRepository, times(2)).findEntertainmentActivityEntityByName(any());
        verify(entertainmentActivityRepository, times(0)).save(any(EntertainmentActivityEntity.class));
        verify(userDetailsRepository, times(1)).save(any(UserDetailsEntity.class));
        verify(entertainmentPlaceRepository, times(1)).save(any(EntertainmentPlaceEntity.class));
        verify(addressRepository, times(1)).save(any());
        verify(entertainmentActivityPlaceRepository, times(1)).saveAll(anyCollection());
    }

    @Test
    void whenSaveEntertainmentPlaceAndUserExistsAndEntertainmentActivityDoesNotExists_thenReturnNothing() {
        when(userDetailsRepository.findById(any())).thenReturn(Optional.of(mockUserDetailsEntity));
        when(entertainmentActivityRepository.findEntertainmentActivityEntityByName(any())).thenReturn(null);
        when(entertainmentActivityRepository.save(any(EntertainmentActivityEntity.class))).thenReturn(null);//we dont care for the result
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null); //we dont care for the result
        when(entertainmentPlaceRepository.save(any(EntertainmentPlaceEntity.class))).thenReturn(null); //we dont care for the result
        when(addressRepository.save(any())).thenReturn(null); //we dont care for the result
        when(entertainmentActivityPlaceRepository.saveAll(anyCollection())).thenReturn(null); //we dont care for the result
        final EntertainmentPlaceInputDTO place = entertainmentPlaceFactory.createMockEntertainmentPlaceInputDTO();
        entertainmentPlaceService.saveEntertainmentPlace(place);
        verify(userDetailsRepository, times(1)).findById(any());
        verify(entertainmentActivityRepository, times(2)).findEntertainmentActivityEntityByName(any());
        verify(entertainmentActivityRepository, times(1)).save(any(EntertainmentActivityEntity.class));
        verify(userDetailsRepository, times(1)).save(any(UserDetailsEntity.class));
        verify(entertainmentPlaceRepository, times(1)).save(any(EntertainmentPlaceEntity.class));
        verify(addressRepository, times(1)).save(any());
        verify(entertainmentActivityPlaceRepository, times(1)).saveAll(anyCollection());
    }

    @Test
    void whenSaveEntertainmentPlaceAndUserDoesNotExists_thenExceptionIsThrown() {
        when(userDetailsRepository.findById(any())).thenReturn(Optional.empty());
        when(entertainmentActivityRepository.findEntertainmentActivityEntityByName(any())).thenReturn(null);
        when(entertainmentActivityRepository.save(any(EntertainmentActivityEntity.class))).thenReturn(null);//we dont care for the result
        when(userDetailsRepository.save(any(UserDetailsEntity.class))).thenReturn(null); //we dont care for the result
        when(entertainmentPlaceRepository.save(any(EntertainmentPlaceEntity.class))).thenReturn(null); //we dont care for the result
        when(addressRepository.save(any())).thenReturn(null); //we dont care for the result
        when(entertainmentActivityPlaceRepository.saveAll(anyCollection())).thenReturn(null); //we dont care for the result
        try {
            final EntertainmentPlaceInputDTO place = entertainmentPlaceFactory.createMockEntertainmentPlaceInputDTO();
            entertainmentPlaceService.saveEntertainmentPlace(place);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(userDetailsRepository, times(1)).findById(any());
        verify(entertainmentActivityRepository, times(0)).findEntertainmentActivityEntityByName(any());
        verify(entertainmentActivityRepository, times(0)).save(any(EntertainmentActivityEntity.class));
        verify(userDetailsRepository, times(0)).save(any(UserDetailsEntity.class));
        verify(entertainmentPlaceRepository, times(0)).save(any(EntertainmentPlaceEntity.class));
        verify(addressRepository, times(0)).save(any());
        verify(entertainmentActivityPlaceRepository, times(0)).saveAll(anyCollection());
    }

    @Test
    void whenDeleteEntertainmentPlaceByIdAndEntertainmentPlaceExists_thenReturnNothing() {
        when(entertainmentPlaceRepository.findById(anyLong())).thenReturn(Optional.of(mockEntertainmentPlaceEntity));
        when(reservationRepository.findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceId(anyLong())).thenReturn(mockReservationEntities);
        doNothing().when(reservationRepository).deleteAll(anyCollection());
        doNothing().when(entertainmentPlaceRepository).delete(any(EntertainmentPlaceEntity.class));
        entertainmentPlaceService.deleteEntertainmentPlaceById(1L);
        verify(entertainmentPlaceRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceId(anyLong());
        verify(reservationRepository, times(1)).deleteAll(anyCollection());
        verify(entertainmentPlaceRepository, times(1)).delete(any(EntertainmentPlaceEntity.class));
    }

    @Test
    void whenDeleteEntertainmentPlaceByIdAndEntertainmentPlaceDoesNotExists_thenExceptionIsThrown() {
        when(entertainmentPlaceRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(reservationRepository.findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceId(anyLong())).thenReturn(mockReservationEntities);
        doNothing().when(reservationRepository).deleteAll(anyCollection());
        doNothing().when(entertainmentPlaceRepository).delete(any(EntertainmentPlaceEntity.class));
        try {
            entertainmentPlaceService.deleteEntertainmentPlaceById(1L);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentPlaceRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(0)).findReservationEntitiesByEntertainmentActivityPlace_EntertainmentPlaceId(anyLong());
        verify(reservationRepository, times(0)).deleteAll(anyCollection());
        verify(entertainmentPlaceRepository, times(0)).delete(any(EntertainmentPlaceEntity.class));
    }

    @Test
    void whenFindByIdAndEntertainmentPlaceExists_thenReturnMockEntertainmentPlace() {
        when(entertainmentPlaceRepository.findById(anyLong())).thenReturn(Optional.of(mockEntertainmentPlaceEntity));
        final EntertainmentPlaceDTO place = entertainmentPlaceService.findById(1L);
        assertNotNull(place);
        assertEquals(place.getName(), "place");
        verify(entertainmentPlaceRepository, times(1)).findById(anyLong());
    }

    @Test
    void whenFindByIdAndEntertainmentPlaceDoesNotExists_thenExceptionIsThrown() {
        when(entertainmentPlaceRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            final EntertainmentPlaceDTO place = entertainmentPlaceService.findById(1L);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentPlaceRepository, times(1)).findById(anyLong());
    }

    @Test
    void whenUpdateEntertainmentPlaceAndEntertainmentPlaceExists_thenReturnNothing() {
        when(entertainmentPlaceRepository.findById(any())).thenReturn(Optional.of(mockEntertainmentPlaceEntity));
        when(entertainmentPlaceRepository.save(any(EntertainmentPlaceEntity.class))).thenReturn(null); //we dont care for the result
        final EntertainmentPlaceDTO place = entertainmentPlaceFactory.createSimpleMockEntertainmentPlaceDTO();
        entertainmentPlaceService.updateEntertainmentPlace(place);
        verify(entertainmentPlaceRepository, times(1)).findById(any());
        verify(entertainmentPlaceRepository, times(1)).save(any(EntertainmentPlaceEntity.class));
    }

    @Test
    void whenUpdateEntertainmentPlaceAndEntertainmentPlaceDoesNotExists_thenExceptionIsThrown() {
        when(entertainmentPlaceRepository.findById(any())).thenReturn(Optional.empty());
        when(entertainmentPlaceRepository.save(any(EntertainmentPlaceEntity.class))).thenReturn(null); //we dont care for the result
        try {
            final EntertainmentPlaceDTO place = entertainmentPlaceFactory.createSimpleMockEntertainmentPlaceDTO();
            entertainmentPlaceService.updateEntertainmentPlace(place);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentPlaceRepository, times(1)).findById(any());
        verify(entertainmentPlaceRepository, times(0)).save(any(EntertainmentPlaceEntity.class));
    }

    @Test
    void whenSearchEntertainmentPlaceByName_thenReturnMockEntertainmentPlaces() {
        when(entertainmentPlaceRepository.findEntertainmentPlacesByName(anyString())).thenReturn(mockEntertainmentPlaceEntities);
        final List<EntertainmentPlaceDTO> places = entertainmentPlaceService.searchEntertainmentPlacesByName("Name");
        assertNotNull(places);
        assertEquals(places.size(), 1);
        verify(entertainmentPlaceRepository, times(1)).findEntertainmentPlacesByName(anyString());
    }

    @Test
    void whenSearchEntertainmentPlacesByActivity_thenReturnMockEntertainmentPlaces() {
        when(entertainmentPlaceRepository.findEntertainmentPlacesByEntertainmentActivity(anyString())).thenReturn(mockEntertainmentPlaceEntities);
        final List<EntertainmentPlaceDTO> places = entertainmentPlaceService.searchEntertainmentPlacesByActivity("Name");
        assertNotNull(places);
        assertEquals(places.size(), 1);
        verify(entertainmentPlaceRepository, times(1)).findEntertainmentPlacesByEntertainmentActivity(anyString());
    }

    @Test
    void whenSearchEntertainmentPlacesByNameAndActivity_thenReturnMockEntertainmentPlaces() {
        when(entertainmentPlaceRepository.fineEntertainmentPlacesByActivityAndName(anyString(), anyString())).thenReturn(mockEntertainmentPlaceEntities);
        final List<EntertainmentPlaceDTO> places = entertainmentPlaceService.searchEntertainmentPlacesByNameAndActivity("name", "activity");
        assertNotNull(places);
        assertEquals(places.size(), 1);
        verify(entertainmentPlaceRepository, times(1)).fineEntertainmentPlacesByActivityAndName(anyString(), anyString());
    }

    @Test
    void whenFindEntertainmentPlaceByNameAndEntertainmentPlaceExists_thenReturnMockEntertainmentPlace() {
        when(entertainmentPlaceRepository.findEntertainmentPlaceEntityByName(anyString())).thenReturn(mockEntertainmentPlaceEntity);
        final EntertainmentPlaceDTO place = entertainmentPlaceService.findEntertainmentPlaceByName("name");
        assertNotNull(place);
        verify(entertainmentPlaceRepository, times(1)).findEntertainmentPlaceEntityByName(anyString());
    }

    @Test
    void whenFindEntertainmentPlaceByNameAndEntertainmentPlaceDoesNotExists_thenExceptionIsThrown() {
        when(entertainmentPlaceRepository.findEntertainmentPlaceEntityByName(anyString())).thenReturn(null);
        try {
            final EntertainmentPlaceDTO place = entertainmentPlaceService.findEntertainmentPlaceByName("name");
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            assertTrue(e.getMessage().contains("not found"));
        }
        verify(entertainmentPlaceRepository, times(1)).findEntertainmentPlaceEntityByName(anyString());
    }
}