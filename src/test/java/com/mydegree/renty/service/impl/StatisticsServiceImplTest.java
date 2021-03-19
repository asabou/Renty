package com.mydegree.renty.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class StatisticsServiceImplTest extends AbstractTest {
    @BeforeEach
    void setUp() {
    }

    @Test
    void findTopMostRentedEntertainmentActivities() {
    }

    @Test
    void findTopMostRentedEntertainmentPlaces() {
    }

    @Test
    void findTopMostRentedHourReservations() {
    }

    @Test
    void findTopMostRentedDateReservations() {
    }
}