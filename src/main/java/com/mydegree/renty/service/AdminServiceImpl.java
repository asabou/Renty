package com.mydegree.renty.service;

import com.google.common.collect.Sets;
import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.IRoleRepository;
import com.mydegree.renty.dao.repository.IUserDetailsRepository;
import com.mydegree.renty.dao.repository.IUserRepository;
import com.mydegree.renty.exceptions.BadRequestException;
import com.mydegree.renty.exceptions.InternalServerError;
import com.mydegree.renty.exceptions.NotFoundException;
import com.mydegree.renty.service.helper.RoleTransformer;
import com.mydegree.renty.service.helper.UserDetailsTransformer;
import com.mydegree.renty.service.helper.UserTransformer;
import com.mydegree.renty.service.model.RoleDTO;
import com.mydegree.renty.service.model.UserDTO;
import com.mydegree.renty.service.model.UserDetailsDTO;
import com.mydegree.renty.utils.Base64Utils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AdminServiceImpl extends AbstractServiceImpl implements IAdminService {
    private final IUserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;

    public AdminServiceImpl(IUserRepository userRepository,
                            IUserDetailsRepository userDetailsRepository,
                            PasswordEncoder passwordEncoder,
                            IRoleRepository roleRepository) {
        super(userRepository);
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    /**
     * @param userDetails User to be created
     * When an owner user is created, then he will have OWNER and RENTER roles with the corresponding permissions
     */
    @Override
    public void saveUserOwner(UserDetailsDTO userDetails) {
        final UserDTO userDTO = userDetails.getUser();
        final String username = userDTO.getUsername();
        final UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity != null) {
            throw new BadRequestException("This user already exists!");
        }
        final RoleEntity renterRole = roleRepository.findRoleEntityByRole("RENTER");
        final RoleEntity ownerRole = roleRepository.findRoleEntityByRole("OWNER");
        if (renterRole == null || ownerRole == null) {
            throw new InternalServerError("Cannot find OWNER or RENTER role!");
        }
        final UserEntity user = UserTransformer.transformUser(userDTO);
        user.setAuthorities(Sets.newHashSet(renterRole, ownerRole));
        final String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(Base64Utils.decode(password)));
        userRepository.save(user);
        userDetailsRepository.save(UserDetailsTransformer.transformUserDetails(userDetails));
    }

    @Override
    public void saveUserAdmin(UserDTO user) {
        final UserEntity userEntity = userRepository.findUserByUsername(user.getUsername());
        if (userEntity != null) {
            throw new BadRequestException("This user already exists!");
        }
        userRepository.save(UserTransformer.transformUser(user));
    }

    @Override
    public List<UserDTO> findAllUsers() {
        Iterable<UserEntity> all = userRepository.findAll();
        return UserTransformer.transformUserEntities(all);
    }

    @Override
    public void updateRolesForUser(String username, Set<RoleDTO> roles) {
        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new NotFoundException("Cannot find user with username: " + username);
        }
        user.setAuthorities(RoleTransformer.transformRoles(roles));
        userRepository.save(user);
    }
}
