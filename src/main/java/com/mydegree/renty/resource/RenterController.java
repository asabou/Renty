package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IReservationService;
import com.mydegree.renty.service.impl.UserServiceImpl;
import com.mydegree.renty.service.model.ReservationInputDTO;
import com.mydegree.renty.service.model.ReservationOutputDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/renter")
public class RenterController {
    private final UserServiceImpl userService;
    private final IReservationService reservationService;

    public RenterController(UserServiceImpl userService, IReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

    @GetMapping("/find-user-by-id")
    private UserDetailsDTO findUserDetailsByUserId(@RequestParam(name = "id") Long id) {
        return userService.findUserDetailsById(id);
    }

    @PutMapping("/update-user-details")
    private UserDetailsDTO updateUser(@RequestBody UserDetailsDTO userDetailsDTO) {
        return userService.updateUser(userDetailsDTO);
    }

    @DeleteMapping("/delete-account-by-id")
    private void deleteAccount(@RequestParam(name = "id") Long id) {
        userService.deleteUserById(id);
    }

    @DeleteMapping("/delete-account-by-username")
    private void deleteAccount(@RequestParam(name = "username") String username) { userService.deleteUserByUsername(username);}

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
}
