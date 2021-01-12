package com.mydegree.renty.resource;

import com.mydegree.renty.service.impl.UserServiceImpl;
import com.mydegree.renty.service.model.UserDetailsDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/renter")
public class RenterController {
    private final UserServiceImpl userService;

    public RenterController(UserServiceImpl userService) {
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

    @DeleteMapping("/delete-account-by-id")
    private void deleteAccount(@RequestParam(name = "id") Long id) {
        userService.deleteUserById(id);
    }

    @DeleteMapping("/delete-account-by-username")
    private void deleteAccount(@RequestParam(name = "username") String username) { userService.deleteUserByUsername(username);}

}
