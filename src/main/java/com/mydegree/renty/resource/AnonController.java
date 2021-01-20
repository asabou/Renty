package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.abstracts.IUserService;
import com.mydegree.renty.service.model.EntertainmentPlaceDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anon")
public class AnonController {
    private final IUserService userService;
    private final IEntertainmentPlaceService entertainmentPlaceService;

    public AnonController(IUserService userService, IEntertainmentPlaceService entertainmentPlaceService) {
        this.userService = userService;
        this.entertainmentPlaceService = entertainmentPlaceService;
    }

    @PostMapping("/create-account")
    private void saveUserAnon(@RequestBody UserDetailsDTO userDetailsDTO) {
        userService.createRenterUser(userDetailsDTO);
    }

    @GetMapping("/all-entertainment-places")
    private List<EntertainmentPlaceDTO> findAllEntertainmentPlaces() {
        return entertainmentPlaceService.findAllEntertainmentPlaces();
    }

    @GetMapping("/all-entertainment-places-by")
    private List<EntertainmentPlaceDTO> findAllEntertainmentPlacesByFilter(@RequestParam(name = "filter") String filter) {
        return entertainmentPlaceService.findAllEntertainmentPlacesByAddressOrNameOrDescriptionOrUserDetailsFirstNameOrUserDetailsLastName(filter);
    }


}
