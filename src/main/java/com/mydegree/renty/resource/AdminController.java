package com.mydegree.renty.resource;

import com.mydegree.renty.service.abstracts.IEntertainmentActivityService;
import com.mydegree.renty.service.impl.AdminServiceImpl;
import com.mydegree.renty.service.model.EntertainmentActivityDTO;
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
    private final AdminServiceImpl adminService;
    private final IEntertainmentActivityService entertainmentActivityService;

    public AdminController(AdminServiceImpl adminService, IEntertainmentActivityService entertainmentActivityService) {
        this.adminService = adminService;
        this.entertainmentActivityService = entertainmentActivityService;
    }

    @PostMapping("/create-user-admin")
    private void createAdminAccount(@RequestBody UserDTO userDTO) {
        adminService.saveUserAdmin(userDTO);
    }

    @PostMapping("/create-user-owner")
    private void createOwnerAccount(@RequestBody UserDetailsDTO userDetailsDTO) {
        adminService.saveUserOwner(userDetailsDTO);
    }

    @DeleteMapping("/delete-account-by-id")
    private void deleteAccount(@RequestParam(name = "id") Long id) {
        adminService.deleteUserById(id);
    }

    @DeleteMapping("/delete-account-by-username")
    private void deleteAccount(@RequestParam(name = "username") String username) {
        adminService.deleteUserByUsername(username);
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

    @PostMapping("/create-entertainment-activity")
    private void createEntertainmentActivity(@RequestBody EntertainmentActivityDTO entertainmentActivityDTO) {
        entertainmentActivityService.saveEntertainmentActivity(entertainmentActivityDTO);
    }
}
