package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IEntertainmentActivityService;
import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.abstracts.IReservationService;
import com.mydegree.renty.service.impl.UserServiceImpl;
import com.mydegree.renty.service.model.*;
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

    @GetMapping("/find-user-by-id")
    private UserDetailsDTO findUserDetailsByUserId(@RequestParam(name = "id") Long id) {
        return userService.findUserByUserId(id);
    }

    @PutMapping("/update-user-details")
    private void updateUser(@RequestBody UserDetailsDTO userDetailsDTO) {
        userService.updateUser(userDetailsDTO);
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

    @DeleteMapping("/cancel-reservation")
    private void cancelReservation(@RequestParam(name = "id") Long id) {
        reservationService.cancelReservation(id);
    }

    @GetMapping("/all-active-reservations")
    private List<ReservationOutputDTO> findAllActiveReservations(@RequestParam(name = "id") Long id) {
        return reservationService.findAllActiveReservationsByUserId(id);
    }

    @GetMapping("/search-entertainment-place")
    private List<EntertainmentPlaceDTO> searchEntertainmentPlace(@RequestParam(name = "filter") String filter) {
        return entertainmentPlaceService.findAllEntertainmentPlacesByAddressOrNameOrDescriptionOrUserDetailsFirstNameOrUserDetailsLastName(filter);
    }

    @PutMapping("/reset-password")
    private void resetPassword(@RequestBody UserDTO userDTO) {
        userService.resetPassword(userDTO);
    }

    @GetMapping("/get-entertainment-activities-for-place")
    private List<EntertainmentActivityDTO> findEntertainmentActivitiesForPlace(@RequestParam(name = "id") Long id) {
        return entertainmentActivityService.findEntertainmentActivitiesByEntertainmentPlaceId(id);
    }
}
