package com.mydegree.renty.resource;

import com.mydegree.renty.service.IAdminService;
import com.mydegree.renty.service.model.Role;
import com.mydegree.renty.service.model.User;
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
    private void saveUser(@RequestBody User user) {
        adminService.saveUser(user);
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
    //TODO need to reconsider the set of roles that are sent from client
    private void updateRoleForUser(@RequestParam(name = "username") String username, Set<Role> roles) {
        adminService.updateRolesForUser(username, roles);
    }

    @GetMapping("/all-users")
    private List<User> getAllUsers() {
        return adminService.findAllUsers();
    }
}
