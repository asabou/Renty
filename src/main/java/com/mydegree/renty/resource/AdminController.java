package com.mydegree.renty.resource;

import com.mydegree.renty.service.IAdminService;
import com.mydegree.renty.service.model.RoleDTO;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.utils.ServicesUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {
    public IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/save-user")
    private void saveUser(@RequestBody UserDTO user) {
        adminService.saveUserAdmin(user);
    }

    @DeleteMapping("/delete-user-by-id")
    private void deleteUserWithId(@RequestParam(name = "id") Long id) {
        adminService.deleteUserByUserId(id);
    }

    @DeleteMapping("/delete-user-by-username")
    private void deleteUserWithUsername(@RequestParam(name = "username") String username) {
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
