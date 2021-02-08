package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.abstracts.IReservationService;
import com.mydegree.renty.service.impl.UserServiceImpl;
import com.mydegree.renty.service.model.EntertainmentPlaceDTO;
import com.mydegree.renty.service.model.EntertainmentPlaceInputDTO;
import com.mydegree.renty.service.model.ReservationOutputDTO;
import com.mydegree.renty.utils.Constants;
import com.mydegree.renty.utils.ServicesUtils;
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

    @PostMapping("/create-entertainment-place")
    private void createEntertainmentPlace(@RequestBody EntertainmentPlaceInputDTO entertainmentPlace) {
        entertainmentPlaceService.saveEntertainmentPlace(entertainmentPlace);
    }

    @GetMapping("/all-owned-entertainment-places")
    private List<EntertainmentPlaceDTO> findAllOwnedEntertainmentPlaces(@RequestHeader(name = "Authorization") String authorization) {
        final String token = authorization.split(" ")[1];
        return entertainmentPlaceService.findAllOwnedEntertainmentPlaces(token);
    }

    @GetMapping("/all-active-reservations-from-owner")
    private List<ReservationOutputDTO> findAllActiveReservationsFromAnOwner(@RequestHeader(name = "Authorization") String authorization) {
        final String token = authorization.split(" ")[1];
        return reservationService.findAllActiveReservationsFromAnOwner(token);
    }

    @DeleteMapping("/delete-entertainment-place")
    private void deleteEntertainmentPlaceById(@RequestParam(name = "id") String id, @RequestParam(name = "name") String name) {
        if (!ServicesUtils.isStringNullOrEmpty(name)) {
            entertainmentPlaceService.deleteEntertainmentPlaceByName(name);
        } else {
            if (!ServicesUtils.isStringNullOrEmpty(id)) {
                entertainmentPlaceService.deleteEntertainmentPlaceById(ServicesUtils.convertStringToLong(id));
            } else {
                userService.throwBadRequestException(Constants.INVALID_QUERY_PARAMS);
            }
        }
    }
}
