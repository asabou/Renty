package com.mydegree.renty.resource;

import com.mydegree.renty.service.UserServiceImpl;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    private final UserServiceImpl userService;

    public OwnerController(UserServiceImpl userService) {
        this.userService = userService;
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
    @DeleteMapping("delete-account-by-username")
    private void deleteAccount(@RequestParam(name = "username") String username) {
        userService.deleteUserByUsername(username);
    }
}
