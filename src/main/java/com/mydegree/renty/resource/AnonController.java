package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IEntertainmentActivityService;
import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.abstracts.IUserService;
import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import com.mydegree.renty.service.model.EntertainmentPlaceDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;
import com.mydegree.renty.utils.ServicesUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anon")
public class AnonController {
    private final IUserService userService;
    private final IEntertainmentPlaceService entertainmentPlaceService;
    private final IEntertainmentActivityService entertainmentActivityService;

    public AnonController(IUserService userService, IEntertainmentPlaceService entertainmentPlaceService,
                          IEntertainmentActivityService entertainmentActivityService) {
        this.userService = userService;
        this.entertainmentPlaceService = entertainmentPlaceService;
        this.entertainmentActivityService = entertainmentActivityService;
    }

    @PostMapping("/create-account")
    private void saveUserAnon(@RequestBody UserDetailsDTO userDetailsDTO) {
        userService.createRenterUser(userDetailsDTO);
    }

    @GetMapping("/all-entertainment-places")
    private List<EntertainmentPlaceDTO> findAllEntertainmentPlaces() {
        return entertainmentPlaceService.findAllEntertainmentPlaces();
    }

    @GetMapping("/all-entertainment-activities")
    private List<EntertainmentActivityDTO> findAllEntertainmentActivities() {
        return entertainmentActivityService.findAll();
    }

    @GetMapping("/entertainment-activities-by-entertainment-place/{id}")
    private List<EntertainmentActivityDTO> getEntertainmentActivitiesByEntertainmentPlace(@PathVariable("id") Long id) {
        return entertainmentActivityService.findEntertainmentActivitiesByEntertainmentPlaceId(id);
    }

    @GetMapping("/search-for-entertainment-places")
    private List<EntertainmentPlaceDTO> searchEntertainmentPlace(@RequestParam(required = false) String name,
                                                                 @RequestParam(required = false) String activity) {
        if (!ServicesUtils.isStringNullOrEmpty(name) && !ServicesUtils.isStringNullOrEmpty(activity) && !name.equals("undefined") && !activity.equals("undefined")) {
            return entertainmentPlaceService.searchEntertainmentPlacesByNameAndActivity(name, activity);
        }
        if ((ServicesUtils.isStringNullOrEmpty(name) && ServicesUtils.isStringNullOrEmpty(activity)) || (name.equals("undefined") && activity.equals(
                "undefined"))) {
            return entertainmentPlaceService.findAllEntertainmentPlaces();
        }
        if (ServicesUtils.isStringNullOrEmpty(name) || name.equals("undefined")) {
            return entertainmentPlaceService.searchEntertainmentPlacesByActivity(activity);
        }
        if (ServicesUtils.isStringNullOrEmpty(activity) || activity.equals("undefined")) {
            return entertainmentPlaceService.searchEntertainmentPlacesByName(name);
        }
        return entertainmentPlaceService.findAllEntertainmentPlaces();
    }
}
