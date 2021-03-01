package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IEntertainmentActivityService;
import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.abstracts.IReservationService;
import com.mydegree.renty.service.impl.UserServiceImpl;
import com.mydegree.renty.service.model.*;
import com.mydegree.renty.utils.ServicesUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/renter")
public class RenterController {
    private final UserServiceImpl userService;
    private final IReservationService reservationService;
    private final IEntertainmentPlaceService entertainmentPlaceService;
    private final IEntertainmentActivityService entertainmentActivityService;

    public RenterController(UserServiceImpl userService, IReservationService reservationService, IEntertainmentPlaceService entertainmentPlaceService, IEntertainmentActivityService entertainmentActivityService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.entertainmentPlaceService = entertainmentPlaceService;
        this.entertainmentActivityService = entertainmentActivityService;
    }

    @DeleteMapping("/delete-account")
    private void deleteAccount(@RequestHeader(name = "Authorization") String authorization) {
        final String token = authorization.split(" ")[1];
        userService.deleteAccount(token);
    }

    @PutMapping("/update-account")
    private void updateAccount(@RequestBody UserDetailsDTO userDetails) {
        userService.updateAccount(userDetails);
    }

    @PostMapping("/create-reservation")
    private void createReservation(@RequestBody ReservationInputDTO reservationInputDTO) {
        reservationService.saveReservation(reservationInputDTO);
    }

    @DeleteMapping("/cancel-reservation/{id}")
    private void cancelReservation(@PathVariable("id") Long id) {
        reservationService.cancelReservation(id);
    }

    @GetMapping("/all-active-reservations")
    private List<ReservationOutputDTO> findAllActiveReservations(@RequestHeader(name = "Authorization") String authorization) {
        final String token = authorization.split(" ")[1];
        return reservationService.findAllActiveReservationsFromRenter(token);
    }

    @GetMapping("/get-all-active-reservations")
    private List<ReservationOutputDTO> findAllActiveReservations() {
        return reservationService.findAllActiveReservations();
    }

    @GetMapping("/active-reservations-for-activity-place")
    private List<ReservationInputDTO> findAllActiveReservationsForActivityAndPlace(@RequestParam("entertainmentActivity") String entertainmentActivity, @RequestParam("entertainmentPlace") String entertainmentPlace) {
        final EntertainmentActivityPlaceIdDTO ids = new EntertainmentActivityPlaceIdDTO();
        ids.setEntertainmentActivity(ServicesUtils.convertStringToLong(entertainmentActivity));
        ids.setEntertainmentPlace(ServicesUtils.convertStringToLong(entertainmentPlace));
        return reservationService.findAllActiveReservationsByActivityAndPlace(ids);
    }

    @GetMapping("/find-entertainment-place/{id}")
    private EntertainmentPlaceDTO findEntertainmentPlaceById(@PathVariable("id") Long id) {
        return entertainmentPlaceService.findById(id);
    }

    @GetMapping("find-entertainment-activity/{id}")
    private EntertainmentActivityDTO findEntertainmentActivity(@PathVariable("id") Long id) {
        return entertainmentActivityService.findEntertainmentActivityById(id);
    }

    @PutMapping("/reset-password")
    private void resetPassword(@RequestBody UserDTO userDTO) {
        userService.resetPassword(userDTO);
    }

    @GetMapping("/entertainment-activities-by-entertainment-place/{id}")
    private List<EntertainmentActivityOutputDTO> findEntertainmentActivitiesForPlace(@PathVariable("id") Long id) {
        return entertainmentActivityService.findEntertainmentActivitiesDetailsByEntertainmentPlaceId(id);
    }

    @GetMapping("/find-user-details")
    private UserDetailsDTO findUserDetails(@RequestHeader("Authorization") String authorization) {
        final String token = authorization.split(" ")[1];
        return userService.findUserDetailsFromToken(token);
    }
}
