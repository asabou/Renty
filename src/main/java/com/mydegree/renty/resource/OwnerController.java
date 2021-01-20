package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.abstracts.IReservationService;
import com.mydegree.renty.service.impl.UserServiceImpl;
import com.mydegree.renty.service.model.EntertainmentPlaceDTO;
import com.mydegree.renty.service.model.EntertainmentPlaceInputDTO;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    private final UserServiceImpl userService;
    private final IEntertainmentPlaceService entertainmentPlaceService;
    private final IReservationService reservationService;

    public OwnerController(UserServiceImpl userService, IEntertainmentPlaceService entertainmentPlaceService, IReservationService reservationService) {
        this.userService = userService;
        this.entertainmentPlaceService = entertainmentPlaceService;
        this.reservationService = reservationService;
    }

    @DeleteMapping("/delete-account")
    private void deleteAccount(@RequestHeader(name = "Authorization") String authorization) {
        final String token = authorization.split(" ")[1];
        userService.deleteAccount(token);
    }

    @PostMapping("/create-entertainment-place")
    private void createEntertainmentPlace(@RequestBody EntertainmentPlaceInputDTO entertainmentPlace) {
        entertainmentPlaceService.saveEntertainmentPlace(entertainmentPlace);
    }

    @GetMapping("/all-owned-entertainment-places")
    private List<EntertainmentPlaceDTO> findAllOwnedEntertainmentPlaces(@RequestParam(name = "id") Long id) {
        return entertainmentPlaceService.findAllEntertainmentPlacesForAnOwnerId(id);
    }

    @GetMapping("/all-entertainment-places")
    private List<EntertainmentPlaceDTO> findAllEntertainmentPlaces() {
        return entertainmentPlaceService.findAllEntertainmentPlaces();
    }

    @DeleteMapping("/delete-entertainment-place-by-id")
    private void deleteEntertainmentPlaceById(@RequestParam(name = "id") Long id) {
        entertainmentPlaceService.deleteEntertainmentPlaceById(id);
    }

    @DeleteMapping("/delete-entertainment-place-by-name")
    private void deleteEntertainmentPlaceByName(@RequestParam(name = "name") String name) {
        entertainmentPlaceService.deleteEntertainmentPlaceByName(name);
    }

    @PutMapping("/update-account")
    private void updateAccount(@RequestBody UserDetailsDTO userDetails) {
        userService.updateAccount(userDetails);
    }

    @PutMapping("/reset-password")
    private void resetPassword(@RequestBody UserDTO userDTO) {
        userService.resetPassword(userDTO);
    }

}
