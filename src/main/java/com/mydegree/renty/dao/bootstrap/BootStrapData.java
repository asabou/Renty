package com.mydegree.renty.dao.bootstrap;

import com.mydegree.renty.dao.entity.RoleEntity;
import com.mydegree.renty.dao.entity.UserDetailsEntity;
import com.mydegree.renty.dao.entity.UserEntity;
import com.mydegree.renty.dao.repository.IRoleRepository;
import com.mydegree.renty.dao.repository.IUserDetailsRepository;
import com.mydegree.renty.dao.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IUserDetailsRepository userDetailsRepository;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public BootStrapData(IRoleRepository roleRepository,
                         IUserRepository userRepository,
                         PasswordEncoder passwordEncoder,
                         IUserDetailsRepository userDetailsRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsRepository = userDetailsRepository;
    }

    private void saveRoles() {
        final RoleEntity admin = new RoleEntity(), owner = new RoleEntity(), renter = new RoleEntity();
        admin.setRole("ADMIN");
        owner.setRole("OWNER");
        renter.setRole("RENTER");
        roleRepository.save(admin);
        roleRepository.save(owner);
        roleRepository.save(renter);
    }

    private void saveUsers() {
        final UserEntity alex = new UserEntity(), iulia = new UserEntity(), lenuta = new UserEntity();
        final UserDetailsEntity alexDetails = new UserDetailsEntity(), iuliaDetails = new UserDetailsEntity(),
                lenutaDetails = new UserDetailsEntity();
        alex.setUserDetails(alexDetails);
        iulia.setUserDetails(iuliaDetails);
        alexDetails.setUser(alex);
        iuliaDetails.setUser(iulia);
        lenuta.setUserDetails(lenutaDetails);
        lenutaDetails.setUser(lenuta);

        final RoleEntity admin = roleRepository.findRoleEntityByRole("ADMIN");
        final RoleEntity owner = roleRepository.findRoleEntityByRole("OWNER");
        final RoleEntity renter = roleRepository.findRoleEntityByRole("RENTER");

        alex.setUsername("alex");
        alex.setPassword(passwordEncoder.encode("password"));
        alex.getAuthorities().add(admin);
        alex.getAuthorities().add(owner);
        alex.getAuthorities().add(renter);

        iulia.setUsername("iulia");
        iulia.setPassword(passwordEncoder.encode("password"));
        iulia.getAuthorities().add(owner);
        iulia.getAuthorities().add(renter);

        lenuta.setUsername("lenuta");
        lenuta.setPassword(passwordEncoder.encode("password"));
        lenuta.getAuthorities().add(renter);

        alexDetails.setEmail("alexmihai906@gmail.com");
        alexDetails.setFirstName("Sabou");
        alexDetails.setLastName("Alexandru");
        alexDetails.setTelNumber("0758866766");

        iuliaDetails.setEmail("iulia.muresan2013@gmail.com");
        iuliaDetails.setFirstName("Muresan");
        iuliaDetails.setLastName("Iulia");
        iuliaDetails.setTelNumber("0773300602");

        lenutaDetails.setEmail("lenuta@gmail.com");
        lenutaDetails.setFirstName("Zaharia");
        lenutaDetails.setLastName("Lenuta");
        lenutaDetails.setTelNumber("0749665288");

        userRepository.save(alex);
        userDetailsRepository.save(alexDetails);
        userRepository.save(iulia);
        userDetailsRepository.save(iuliaDetails);
        userRepository.save(lenuta);
        userDetailsRepository.save(lenutaDetails);
    }

    @Override
    public void run(String... args) {
        //TODO I have to ask somebody which type of database (h2 or a real mysql db) should I use when active profile is test or dev
        if (activeProfile.equals("dev") || activeProfile.equals("test")) {
            saveRoles();
            saveUsers();
        }
        System.out.println(roleRepository.count() + " roles");
        System.out.println(userRepository.count() + " users");
        System.out.println(userDetailsRepository.count() + " user details");
    }
}
