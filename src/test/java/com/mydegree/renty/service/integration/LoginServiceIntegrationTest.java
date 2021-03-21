//package com.mydegree.renty.service.integration;
//
//import com.mydegree.renty.service.impl.LoginServiceImpl;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ActiveProfiles("test")
//public class LoginServiceIntegrationTest extends AbstractIntegrationTest {
//    @Autowired
//    private LoginServiceImpl loginService;
//
//    @Test
//    public void whenLoadByUsernameAndUserExists_thenReturnUser() {
//        final UserDetails userDetails = loginService.loadUserByUsername("alex");
//        assertNotNull(userDetails);
//        assertEquals(userDetails.getUsername(), "alex");
//    }
//
//    @Test
//    public void whenLoadByUsernameAndUserDoesNotExists_thenExceptionIsThrown() {
//        try {
//            final UserDetails userDetails = loginService.loadUserByUsername("andres");
//        } catch (Exception e) {
//            assertTrue(e.getMessage().contains("not found"));
//        }
//    }
//}
