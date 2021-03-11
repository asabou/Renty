package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.dao.repository.*;
import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.abstracts.IReservationService;
import com.mydegree.renty.service.abstracts.IUserService;
import com.mydegree.renty.service.model.*;
import com.mydegree.renty.utils.Base64Utils;
import com.mydegree.renty.utils.DateUtils;
import com.mydegree.renty.utils.ImageBytesUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final IRoleRepository roleRepository;
    private final IUserDetailsRepository userDetailsRepository;
    private final IUserService userService;
    private final EntertainmentActivityServiceImpl entertainmentActivityService;
    private final IEntertainmentActivityRepository entertainmentActivityRepository;
    private final IEntertainmentPlaceService entertainmentPlaceService;
    private final IEntertainmentPlaceRepository entertainmentPlaceRepository;
    private final IReservationRepository reservationRepository;
    private final IReservationService reservationService;
    private final IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository;

    public BootStrapData(IRoleRepository roleRepository,
                         IUserDetailsRepository userDetailsRepository, IUserService userService,
                         EntertainmentActivityServiceImpl entertainmentActivityService,
                         IEntertainmentActivityRepository entertainmentActivityRepository,
                         IEntertainmentPlaceService entertainmentPlaceService,
                         IEntertainmentPlaceRepository entertainmentPlaceRepository,
                         IReservationRepository reservationRepository,
                         IReservationService reservationService,
                         IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository) {
        this.roleRepository = roleRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.userService = userService;
        this.entertainmentActivityService = entertainmentActivityService;
        this.entertainmentActivityRepository = entertainmentActivityRepository;
        this.entertainmentPlaceService = entertainmentPlaceService;
        this.entertainmentPlaceRepository = entertainmentPlaceRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.entertainmentActivityPlaceRepository = entertainmentActivityPlaceRepository;
    }

    private void saveRoles() {
        final RoleEntity adminRole = new RoleEntity(), ownerRole = new RoleEntity(), renterRole = new RoleEntity();
        adminRole.setRole("ADMIN"); adminRole.setDescription("The users with this role can manage the app");
        ownerRole.setRole("OWNER"); ownerRole.setDescription("The users with this role can manage their entertainment places");
        renterRole.setRole("RENTER"); renterRole.setDescription("The users with this role can rent entertainment places");

        roleRepository.save(adminRole);
        roleRepository.save(ownerRole);
        roleRepository.save(renterRole);
    }

    private void saveAdminAlex() {
        final UserDTO adminAlex = new UserDTO();
        final UserDetailsDTO adminAlexDetails = new UserDetailsDTO();

        adminAlex.setUsername("alex");
        adminAlex.setPassword(Base64Utils.encode("password"));
        adminAlexDetails.setFirstName("Alex");
        adminAlexDetails.setLastName("Sabou");
        adminAlexDetails.setUser(adminAlex);

        userService.createAdminUser(adminAlexDetails);
    }

    private void saveAdmins() {
        saveAdminAlex();
    }

    private void saveEntertainmentActivities() {
        final EntertainmentActivityDTO football = new EntertainmentActivityDTO(); football.setName("Football");
        football.setDescription("Description for football");
        final EntertainmentActivityDTO basket = new EntertainmentActivityDTO(); basket.setName("Basket");
        basket.setDescription("Description for basket");
        final EntertainmentActivityDTO handball = new EntertainmentActivityDTO(); handball.setName("Handball");
        handball.setDescription("Description for handball");
        final EntertainmentActivityDTO tennis = new EntertainmentActivityDTO(); tennis.setName("Tennis");
        tennis.setDescription("Description for tennis");
        entertainmentActivityService.saveEntertainmentActivity(football);
        entertainmentActivityService.saveEntertainmentActivity(basket);
        entertainmentActivityService.saveEntertainmentActivity(handball);
        entertainmentActivityService.saveEntertainmentActivity(tennis);
    }

    private void createAdditionalEntertainmentActivitiesForOwnerJuliaPlaces() {
        final EntertainmentActivityInputDTO tennis1 = new EntertainmentActivityInputDTO();
        tennis1.setPrice(40.0);
        tennis1.setMaxPeopleAllowed(4);
        tennis1.setEntertainmentActivityId(4L);
        tennis1.setEntertainmentPlaceId(1L);
        entertainmentActivityService.saveEntertainmentActivityForPlace(tennis1);

        final EntertainmentActivityInputDTO handball1 = new EntertainmentActivityInputDTO();
        handball1.setPrice(65.0);
        handball1.setMaxPeopleAllowed(10);
        handball1.setEntertainmentActivityId(3L);
        handball1.setEntertainmentPlaceId(1L);
        entertainmentActivityService.saveEntertainmentActivityForPlace(handball1);

        final EntertainmentActivityInputDTO tennis2 = new EntertainmentActivityInputDTO();
        tennis2.setPrice(40.0);
        tennis2.setMaxPeopleAllowed(4);
        tennis2.setEntertainmentActivityId(4L);
        tennis2.setEntertainmentPlaceId(2L);
        entertainmentActivityService.saveEntertainmentActivityForPlace(tennis2);
    }

    private void saveOwnerJulia() {
        final UserDTO ownerJulia = new UserDTO();
        final UserDetailsDTO ownerJuliaDetails = new UserDetailsDTO();

        ownerJulia.setUsername("iulia");
        ownerJulia.setPassword(Base64Utils.encode("password"));

        ownerJuliaDetails.setUser(ownerJulia);
        ownerJuliaDetails.setFirstName("Iulia");
        ownerJuliaDetails.setLastName("Ioana");

        userService.createOwnerUser(ownerJuliaDetails); //id 2

        final EntertainmentPlaceInputDTO entertainmentPlaceInputDTO1 = new EntertainmentPlaceInputDTO();
        final AddressDTO addressDTO1 = new AddressDTO();
        entertainmentPlaceInputDTO1.setUserDetailsId(2L); //Iulia's id
        entertainmentPlaceInputDTO1.setName("Name1");
        entertainmentPlaceInputDTO1.setDescription("Description1");
        entertainmentPlaceInputDTO1.setAddress(addressDTO1);
        entertainmentPlaceInputDTO1.setEntertainmentActivity("Football");
        entertainmentPlaceInputDTO1.setPricePerHour(50.0);
        entertainmentPlaceInputDTO1.setMaxPeopleAllowed(12);
        if (activeProfile.equals("dev")) {
            entertainmentPlaceInputDTO1.setProfileImage(new ImageBytesUtils("images/entplace2.jpg").extractBytes());
        }
        addressDTO1.setNumber("12");
        addressDTO1.setStreet("Street1");
        addressDTO1.setCounty("County1");
        addressDTO1.setCity("City1");
        entertainmentPlaceService.saveEntertainmentPlace(entertainmentPlaceInputDTO1);

        final EntertainmentPlaceInputDTO entertainmentPlaceInputDTO2 = new EntertainmentPlaceInputDTO();
        final AddressDTO addressDTO2 = new AddressDTO();
        entertainmentPlaceInputDTO2.setUserDetailsId(2L); //Iulia's id
        entertainmentPlaceInputDTO2.setName("Name2");
        entertainmentPlaceInputDTO2.setDescription("Description2");
        entertainmentPlaceInputDTO2.setAddress(addressDTO2);
        entertainmentPlaceInputDTO2.setEntertainmentActivity("Football");
        entertainmentPlaceInputDTO2.setPricePerHour(60.0);
        entertainmentPlaceInputDTO2.setMaxPeopleAllowed(10);
        if (activeProfile.equals("dev")) {
            entertainmentPlaceInputDTO2.setProfileImage(new ImageBytesUtils("images/entplace3.jpg").extractBytes());
        }
        addressDTO1.setNumber("11");
        addressDTO1.setStreet("Street2");
        addressDTO1.setCounty("County2");
        addressDTO1.setCity("City1");
        entertainmentPlaceService.saveEntertainmentPlace(entertainmentPlaceInputDTO2);
    }

    private void saveOwners() {
        saveOwnerJulia();
    }

    private void saveRenterTudor() {
        final UserDTO renterTudor = new UserDTO();
        final UserDetailsDTO renterTudorDetails = new UserDetailsDTO();

        renterTudor.setUsername("tudor");
        renterTudor.setPassword(Base64Utils.encode("password"));

        renterTudorDetails.setFirstName("Tudor");
        renterTudorDetails.setLastName("Movila");
        renterTudorDetails.setUser(renterTudor);

        userService.createRenterUser(renterTudorDetails);

        final ReservationInputDTO reservation1 = new ReservationInputDTO(); //for julia first place (placeId = 1, userId = 2)
        reservation1.setEntertainmentActivityId(1L);
        reservation1.setEntertainmentPlaceId(1L);
        reservation1.setReservationHour(12);
        reservation1.setReservationDate(DateUtils.parseStringToSqlDate(DateUtils.getCurrentDate().toString()).toString());
        reservation1.setRentalRepresentativeId(3L); //tudor id

        reservationService.saveReservation(reservation1);

        final ReservationInputDTO reservation2 = new ReservationInputDTO(); //for julia first place (placeId = 1, userId = 2)
        reservation2.setEntertainmentActivityId(1L);
        reservation2.setEntertainmentPlaceId(1L);
        reservation2.setReservationHour(13);
        reservation2.setReservationDate(DateUtils.parseStringToSqlDate(DateUtils.getCurrentDate().toString()).toString());
        reservation2.setRentalRepresentativeId(3L);

        reservationService.saveReservation(reservation2);
    }

    private void saveRenters() {
        saveRenterTudor();
    }

    @Override
    public void run(String... args) throws Exception {
        //TODO I have to ask somebody which type of database (h2 or a real mysql db) should I use when active profile is test or dev
        if (activeProfile.equals("dev") || activeProfile.equals("test")) {
            saveEntertainmentActivities();
            saveRoles();
            saveAdmins();
            saveOwners();
            saveRenters();
            createAdditionalEntertainmentActivitiesForOwnerJuliaPlaces();
        }
        System.out.println(roleRepository.count() + " roles");
        System.out.println(userDetailsRepository.count() + " users");
        System.out.println(entertainmentActivityRepository.count() + " entertainment activities");
        System.out.println(entertainmentPlaceRepository.count() + " entertainment places");
        System.out.println(reservationRepository.count() + " reservations");
        System.out.println(entertainmentActivityPlaceRepository.count() + " entertainment activities_places");
    }
}
