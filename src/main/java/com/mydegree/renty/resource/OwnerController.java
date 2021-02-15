package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IEntertainmentActivityService;
import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.abstracts.IReservationService;
import com.mydegree.renty.service.impl.UserServiceImpl;
import com.mydegree.renty.service.model.EntertainmentActivityInputDTO;
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
    private final IEntertainmentActivityService entertainmentActivityService;
    private final IReservationService reservationService;

    public OwnerController(UserServiceImpl userService,
                           IEntertainmentPlaceService entertainmentPlaceService,
                           IEntertainmentActivityService entertainmentActivityService,
                           IReservationService reservationService) {
        this.userService = userService;
        this.entertainmentPlaceService = entertainmentPlaceService;
        this.entertainmentActivityService = entertainmentActivityService;
        this.reservationService = reservationService;
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
    private List<EntertainmentPlaceDTO> findAllOwnedEntertainmentPlaces(@RequestHeader(name = "Authorization") String authorization) {
        final String token = authorization.split(" ")[1];
        return entertainmentPlaceService.findAllOwnedEntertainmentPlaces(token);
    }

    @GetMapping("/all-active-reservations-from-owner")
    private List<ReservationOutputDTO> findAllActiveReservationsFromAnOwner(@RequestHeader(name = "Authorization") String authorization) {
        final String token = authorization.split(" ")[1];
        return reservationService.findAllActiveReservationsFromAnOwner(token);
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
}