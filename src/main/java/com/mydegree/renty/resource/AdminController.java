package com.mydegree.renty.resource;

import com.mydegree.renty.service.IAdminService;
import com.mydegree.renty.service.model.RoleDTO;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;
import com.mydegree.renty.utils.ServicesUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/create-user-admin")
    private void saveUserAdmin(@RequestBody UserDTO userDTO) {
        adminService.saveUserAdmin(userDTO);
    }

    @PostMapping("/create-user-owner")
    private void saveUserOwner(@RequestBody UserDetailsDTO userDetailsDTO) {
        adminService.saveUserOwner(userDetailsDTO);
    }

    @DeleteMapping("/delete-user-by-id")
    private void deleteUserById(@RequestParam(name = "id") Long id) {
        adminService.deleteUserByUserId(id);
    }

    @DeleteMapping("/delete-user-by-username")
    private void deleteUserByUsername(@RequestParam(name = "username") String username) {
        adminService.deleteUserByUserName(username);
    }

    @PostMapping("/update-role-for-user")
    private void updateRoleForUser(@RequestParam(name = "username") String username, @RequestBody List<RoleDTO> roles) {
        final Set<RoleDTO> setRoles = ServicesUtils.convertListToSet(roles);
        adminService.updateRolesForUser(username, setRoles);
    }

    @GetMapping("/all-users")
    private List<UserDTO> getAllUsers() {
        return adminService.findAllUsers();
    }
}
