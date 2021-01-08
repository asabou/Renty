package com.mydegree.renty.resource;

import com.mydegree.renty.service.IUserService;
import com.mydegree.renty.service.model.UserDetailsDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/renter")
public class RenterController {
    private final IUserService userService;

    public RenterController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/find-user-by-id")
    private UserDetailsDTO findUserDetailsByUserId(@RequestParam(name = "id") Long id) {
        return userService.findUserDetailsById(id);
    }

    @PutMapping("/update-user-details")
    private UserDetailsDTO updateUser(@RequestBody UserDetailsDTO userDetailsDTO) {
        return userService.updateUser(userDetailsDTO);
    }

    @DeleteMapping("/delete-account")
    private void deleteAccount(@RequestParam(name = "id") Long id) {
        userService.deleteUser(id);
    }

}
