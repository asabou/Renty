package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IEntertainmentActivityService;
import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.abstracts.IReservationService;
import com.mydegree.renty.service.abstracts.IStatisticsService;
import com.mydegree.renty.service.impl.UserServiceImpl;
import com.mydegree.renty.service.model.*;
import com.mydegree.renty.utils.Constants;
import com.mydegree.renty.utils.RestUtils;
import com.mydegree.renty.utils.ServicesUtils;
import com.mydegree.renty.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    private final UserServiceImpl userService;
    private final IEntertainmentPlaceService entertainmentPlaceService;
    private final IEntertainmentActivityService entertainmentActivityService;
    private final IReservationService reservationService;
    private final IStatisticsService statisticsService;
    private final SecretKey secretKey;

    public OwnerController(UserServiceImpl userService,
                           IEntertainmentPlaceService entertainmentPlaceService,
                           IEntertainmentActivityService entertainmentActivityService,
                           IReservationService reservationService, IStatisticsService statisticsService, SecretKey secretKey) {
        this.userService = userService;
        this.entertainmentPlaceService = entertainmentPlaceService;
        this.entertainmentActivityService = entertainmentActivityService;
        this.reservationService = reservationService;
        this.statisticsService = statisticsService;
        this.secretKey = secretKey;
    }

    @PostMapping("/create-entertainment-place")
    private void createEntertainmentPlace(@RequestBody EntertainmentPlaceInputDTO entertainmentPlace) {
        entertainmentPlaceService.saveEntertainmentPlace(entertainmentPlace);
    }

    @PostMapping("/update-entertainment-place")
    private void updateEntertainmentPlace(@RequestBody EntertainmentPlaceDTO entertainmentPlace) {
        entertainmentPlaceService.updateEntertainmentPlace(entertainmentPlace);
    }

    @GetMapping("/all-owned-entertainment-places")
    private List<EntertainmentPlaceDTO> findAllOwnedEntertainmentPlaces(@RequestHeader(name = Constants.Authorization) String authorization) {
        final Long userId = RestUtils.getUserDetailsIdFromAuthHeaderUsingSecretKey(authorization, secretKey);
        return entertainmentPlaceService.findAllOwnedEntertainmentPlaces(userId);
    }

    @GetMapping("/all-active-reservations-from-owner")
    private List<ReservationOutputDTO> findAllActiveReservationsFromAnOwner(@RequestHeader(name = Constants.Authorization) String authorization) {
        final Long userId = RestUtils.getUserDetailsIdFromAuthHeaderUsingSecretKey(authorization, secretKey);
        return reservationService.findAllActiveReservationsFromAnOwner(userId);
    }

    @DeleteMapping("/delete-entertainment-place/{id}")
    private void deleteEntertainmentPlaceById(@PathVariable("id") String id) {
        if (ServicesUtils.isStringNullOrEmpty(id)) {
            userService.throwBadRequestException(Constants.INVALID_QUERY_PARAMS);
        }
        entertainmentPlaceService.deleteEntertainmentPlaceById(ServicesUtils.convertStringToLong(id));
    }

    @PostMapping("/delete-entertainment-activity-from-place")
    private void deleteEntertainmentActivityFromPlace(@RequestBody EntertainmentActivityInputDTO entertainmentActivityInputDTO) {
        entertainmentActivityService.deleteEntertainmentActivityForPlace(entertainmentActivityInputDTO);
    }

    @PostMapping("/update-entertainment-activity")
    private void updateEntertainmentPlace(@RequestBody EntertainmentActivityInputDTO entertainmentPlaceInputDTO) {
        entertainmentActivityService.updateEntertainmentActivityForPlace(entertainmentPlaceInputDTO);
    }

    @PostMapping("/entertainment-activity-from-place")
    private EntertainmentActivityInputDTO findEntertainmentActivityFromPlace(@RequestBody EntertainmentActivityInputDTO entertainmentActivityInputDTO) {
        return entertainmentActivityService.findEntertainmentActivityForPlace(entertainmentActivityInputDTO);
    }

    @PostMapping("/create-entertainment-activity-for-place")
    private void createEntertainmentActivityForPlace(@RequestBody EntertainmentActivityInputDTO entertainmentActivityInputDTO) {
        entertainmentActivityService.saveEntertainmentActivityForPlace(entertainmentActivityInputDTO);
    }

    @GetMapping("/most-rented-entertainment-activities")
    private List<CustomEntertainmentActivityDTO> getMostRentedEntertainmentActivities(@RequestHeader(name = Constants.Authorization) String authorization,
                                                                                      @RequestParam(required = false) String dateFrom, @RequestParam(required = false) String dateTo) {
        final Long userId = RestUtils.getUserDetailsIdFromAuthHeaderUsingSecretKey(authorization, secretKey);
        return statisticsService.findTopMostRentedEntertainmentActivities(userId, dateFrom, dateTo);
    }

    @GetMapping("/most-rented-entertainment-places")
    private List<CustomEntertainmentPlaceDTO> getMostRentedEntertainmentPlaces(@RequestHeader(name = Constants.Authorization) String authorization,
                                                                          @RequestParam(required = false) String dateFrom, @RequestParam(required = false) String dateTo) {
        final Long userId = RestUtils.getUserDetailsIdFromAuthHeaderUsingSecretKey(authorization, secretKey);
        return statisticsService.findTopMostRentedEntertainmentPlaces(userId, dateFrom, dateTo);
    }

    @GetMapping("/most-rented-reservation-hour")
    private List<CustomReservationHourDTO> getMostRentedReservationHour(@RequestHeader(name = Constants.Authorization) String authorization,
                                                                        @RequestParam(required = false) Long placeId,
                                                                        @RequestParam(required = false) String dateFrom,
                                                                        @RequestParam(required = false) String dateTo) {
        final Long userId = RestUtils.getUserDetailsIdFromAuthHeaderUsingSecretKey(authorization, secretKey);
        return statisticsService.findTopMostRentedHourReservations(userId, placeId, dateFrom, dateTo);
    }

    @GetMapping("/most-rented-reservation-date")
    private List<CustomReservationDateDTO> getMostRentedReservationDate(@RequestHeader(name = Constants.Authorization) String authorization,
                                                                        @RequestParam(required = false) Long placeId) {
        final Long userId = RestUtils.getUserDetailsIdFromAuthHeaderUsingSecretKey(authorization, secretKey);
        return statisticsService.findTopMostRentedDateReservations(userId, placeId);
    }
}