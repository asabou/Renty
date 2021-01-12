package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IEntertainmentPlaceService;
import com.mydegree.renty.service.model.EntertainmentPlaceInputDTO;
import com.mydegree.renty.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    private final UserServiceImpl userService;
    private final IEntertainmentPlaceService entertainmentPlaceService;

    public OwnerController(UserServiceImpl userService, IEntertainmentPlaceService entertainmentPlaceService) {
        this.userService = userService;
        this.entertainmentPlaceService = entertainmentPlaceService;
    }

    /**
     * Delete user; because of the cascade type, all dependent entities will be removed
     * @param id Long
     */
    @DeleteMapping("/delete-account-by-id")
    private void deleteAccount(@RequestParam(name = "id") Long id) {
        userService.deleteUserById(id);
    }

    /**
     * Delete user; because of the cascade type, all dependent entities will be removed
     * @param username String
     */
    @DeleteMapping("/delete-account-by-username")
    private void deleteAccount(@RequestParam(name = "username") String username) {
        userService.deleteUserByUsername(username);
    }

    @PostMapping("/create-entertainment-place")
    private void createEntertainmentPlace(@RequestBody EntertainmentPlaceInputDTO entertainmentPlace) {
        entertainmentPlaceService.saveEntertainmentPlace(entertainmentPlace);
    }
}
