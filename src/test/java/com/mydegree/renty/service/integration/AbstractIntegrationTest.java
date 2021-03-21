//package com.mydegree.renty.service.integration;
//
//import com.mydegree.renty.dao.repository.*;
//import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
//import com.mydegree.renty.service.abstracts.IReservationService;
//import com.mydegree.renty.service.abstracts.IUserService;
//import com.mydegree.renty.service.impl.BootStrapDataFakeObjects;
//import com.mydegree.renty.service.impl.EntertainmentActivityServiceImpl;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class AbstractIntegrationTest {
//    @Autowired
//    private IUserRepository userRepository;
//    @Autowired
//    private IEntertainmentActivityRepository entertainmentActivityRepository;
//    @Autowired
//    private IAddressRepository addressRepository;
//    @Autowired
//    private IRoleRepository roleRepository;
//    @Autowired
//    private IUserDetailsRepository userDetailsRepository;
//    @Autowired
//    private IUserService userService;
//    @Autowired
//    private EntertainmentActivityServiceImpl entertainmentActivityService;
//    @Autowired
//    private IEntertainmentPlaceService entertainmentPlaceService;
//    @Autowired
//    private IEntertainmentPlaceRepository entertainmentPlaceRepository;
//    @Autowired
//    private IReservationRepository reservationRepository;
//    @Autowired
//    private IReservationService reservationService;
//    @Autowired
//    private IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository;
//
//    private void populateH2() throws Exception {
//        BootStrapDataFakeObjects data = new BootStrapDataFakeObjects(roleRepository,
//                userDetailsRepository,
//                userService,
//                entertainmentActivityService,
//                entertainmentActivityRepository,
//                entertainmentPlaceService,
//                entertainmentPlaceRepository,
//                reservationRepository,
//                reservationService,
//                entertainmentActivityPlaceRepository);
//        data.saveEntertainmentActivities();
//        data.saveRoles();
//        data.saveAdmins();
//        data.saveOwners();
//        data.saveRenters();
//        data.createAdditionalEntertainmentActivitiesForOwnerJuliaPlaces();
//        System.out.println("##############################################################");
//        System.out.println("#              SetUp                                         #");
//        System.out.println("##############################################################");
//        count();
//        System.out.println("##############################################################");
//    }
//
//    //@Before
//    public void setUp() {
//        try {
//            populateH2();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void clearH2() {
//        reservationRepository.deleteAll();
//        userRepository.deleteAll();
//        roleRepository.deleteAll();
//        userDetailsRepository.deleteAll();
//        entertainmentPlaceRepository.deleteAll();
//        addressRepository.deleteAll();
//        entertainmentActivityPlaceRepository.deleteAll();
//        entertainmentActivityRepository.deleteAll();
//    }
//
//    //@After
//    public void tearDown() throws Exception {
//        clearH2();
//        System.out.println("##############################################################");
//        System.out.println("#              Tear down                                     #");
//        System.out.println("##############################################################");
//        count();
//        System.out.println("##############################################################");
//    }
//
//    private void count() {
//        System.out.println(roleRepository.count() + " roles");
//        System.out.println(userDetailsRepository.count() + " users");
//        System.out.println(entertainmentActivityRepository.count() + " entertainment activities");
//        System.out.println(entertainmentPlaceRepository.count() + " entertainment places");
//        System.out.println(reservationRepository.count() + " reservations");
//        System.out.println(entertainmentActivityPlaceRepository.count() + " entertainment activities_places");
//    }
//}
