package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IEntertainmentActivityService;
import com.mydegree.renty.service.abstracts.IRoleService;
import com.mydegree.renty.service.impl.AdminServiceImpl;
import com.mydegree.renty.service.impl.UserServiceImpl;
import com.mydegree.renty.service.model.EntertainmentActivityDTO;
import com.mydegree.renty.service.model.RoleDTO;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;
import com.mydegree.renty.utils.ServicesUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userService;
    private final AdminServiceImpl adminService;
    private final IEntertainmentActivityService entertainmentActivityService;
    private final IRoleService roleService;

    public AdminController(UserServiceImpl userService,
                           AdminServiceImpl adminService,
                           IEntertainmentActivityService entertainmentActivityService,
                           IRoleService roleService) {
        this.userService = userService;
        this.adminService = adminService;
        this.entertainmentActivityService = entertainmentActivityService;
        this.roleService = roleService;
    }

    @PostMapping("/create-user-admin")
    private void createAdminAccount(@RequestBody UserDetailsDTO userDetailsDTO) {
        userService.createAdminUser(userDetailsDTO);
    }

    @PostMapping("/create-user-owner")
    private void createOwnerAccount(@RequestBody UserDetailsDTO userDetailsDTO) {
        userService.createOwnerUser(userDetailsDTO);
    }

    @DeleteMapping("/delete-account-by-id")
    private void deleteAccount(@RequestParam(name = "id") Long id) {
        userService.deleteAccountByUserId(id);
    }

    @DeleteMapping("/delete-account-by-username")
    private void deleteAccount(@RequestParam(name = "username") String username) {
        userService.deleteAccountByUsername(username);
    }

    @PostMapping("/update-roles-for-user")
    private void updateRoleForUser(@RequestParam(name = "username") String username,
                                   @RequestParam(name = "id") Long id, @RequestBody List<RoleDTO> roles) {
        if (!ServicesUtils.isStringNullOrEmpty(username)) {
            adminService.updateRolesForUsername(username, roles);
        } else {
            if (id != null) {
                adminService.updateRolesForUserId(id, roles);
            }
        }
    }

    @GetMapping("/all-users")
    private List<UserDTO> getAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping("/create-entertainment-activity")
    private void createEntertainmentActivity(@RequestBody EntertainmentActivityDTO entertainmentActivityDTO) {
        entertainmentActivityService.saveEntertainmentActivity(entertainmentActivityDTO);
    }

    @GetMapping("/all-users-details")
    private List<UserDetailsDTO> getAllUsersDetails() {
        return userService.findAllUserDetails();
    }

    @GetMapping("/all-roles")
    private List<RoleDTO> getAllRoles() {
        return roleService.findAllRoles();
    }

    @PutMapping("/update-account")
    private void updateAccount(@RequestBody UserDetailsDTO userDetails) {
        userService.updateAccount(userDetails);
    }

    @PutMapping("/reset-password")
    private void resetPassword(@RequestBody UserDTO userDTO) {
        userService.resetPassword(userDTO);
    }

}
