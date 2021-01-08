package com.mydegree.renty.resource;

import com.mydegree.renty.service.IUserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    private final IUserService userService;

    public OwnerController(IUserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/delete-account")
    private void deleteAccount(@RequestParam(name = "id") Long id) {
        userService.deleteUser(id); //because of the cascade type in dao, all dependent entities will be removed
    }
}
