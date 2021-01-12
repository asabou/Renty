package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IUserService;
import com.mydegree.renty.service.model.UserDetailsDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anon")
public class AnonController {
    private final IUserService userService;

    public AnonController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-account")
    private void saveUserAnon(@RequestBody UserDetailsDTO userDetailsDTO) {
        userService.saveUserAnon(userDetailsDTO);
    }

}
