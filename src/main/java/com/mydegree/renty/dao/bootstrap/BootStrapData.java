package com.mydegree.renty.dao.bootstrap;

import com.google.common.collect.Sets;
import com.mydegree.renty.dao.entity.*;
import com.mydegree.renty.dao.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.Set;

@Component
public class BootStrapData implements CommandLineRunner {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IUserDetailsRepository userDetailsRepository;
    private final IEntertainmentActivityRepository entertainmentActivityRepository;
    private final IEntertainmentPlaceRepository entertainmentPlaceRepository;
    private final IAddressRepository addressRepository;
    private final IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public BootStrapData(IRoleRepository roleRepository,
                         IUserRepository userRepository,
                         PasswordEncoder passwordEncoder,
                         IUserDetailsRepository userDetailsRepository,
                         IEntertainmentActivityRepository entertainmentActivityRepository,
                         IEntertainmentPlaceRepository entertainmentPlaceRepository,
                         IAddressRepository addressRepository,
                         IEntertainmentActivityPlaceRepository entertainmentActivityPlaceRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsRepository = userDetailsRepository;
        this.entertainmentActivityRepository = entertainmentActivityRepository;
        this.entertainmentPlaceRepository = entertainmentPlaceRepository;
        this.addressRepository = addressRepository;
        this.entertainmentActivityPlaceRepository = entertainmentActivityPlaceRepository;
    }

    private void saveRoles() {
        final RoleEntity adminRole = new RoleEntity(), ownerRole = new RoleEntity(), renterRole = new RoleEntity();
        adminRole.setRole("ADMIN");
        ownerRole.setRole("OWNER");
        renterRole.setRole("RENTER");

        roleRepository.save(adminRole);
        roleRepository.save(ownerRole);
        roleRepository.save(renterRole);
    }

    private void saveEntertainmentActivities() {
        final EntertainmentActivityEntity football = new EntertainmentActivityEntity();
        final EntertainmentActivityEntity handball = new EntertainmentActivityEntity();
        final EntertainmentActivityEntity volley = new EntertainmentActivityEntity();
        final EntertainmentActivityEntity basket = new EntertainmentActivityEntity();
        football.setName("Football");
        handball.setName("Handball");
        volley.setName("Volley");
        basket.setName("Basket");
        entertainmentActivityRepository.save(football);
        entertainmentActivityRepository.save(handball);
        entertainmentActivityRepository.save(volley);
        entertainmentActivityRepository.save(basket);
    }

    private void saveAdminAlex() {
        final UserEntity adminAlex = new UserEntity();
        final RoleEntity roleAdmin = roleRepository.findRoleEntityByRole("ADMIN");
        final RoleEntity roleRenter = roleRepository.findRoleEntityByRole("RENTER");

        adminAlex.setUsername("alex");
        adminAlex.setPassword(passwordEncoder.encode("password"));
        adminAlex.setAuthorities(Sets.newHashSet(roleAdmin, roleRenter));
        userRepository.save(adminAlex);
    }

    private void saveAdminJulia() {
        final UserEntity adminJulia = new UserEntity();
        final RoleEntity roleAdmin = roleRepository.findRoleEntityByRole("ADMIN");
        final RoleEntity roleRenter = roleRepository.findRoleEntityByRole("RENTER");

        adminJulia.setUsername("julia");
        adminJulia.setPassword(passwordEncoder.encode("password"));
        adminJulia.setAuthorities(Sets.newHashSet(roleAdmin, roleRenter));
        userRepository.save(adminJulia);
    }

    private void saveAdmins() {
        saveAdminAlex();
        saveAdminJulia();
    }

    private void saveOwnerMihai() {
        final RoleEntity renterRole = roleRepository.findRoleEntityByRole("RENTER");
        final RoleEntity ownerRole = roleRepository.findRoleEntityByRole("OWNER");
        final EntertainmentActivityEntity football = entertainmentActivityRepository.findEntertainmentActivityEntityByName("Football");
        final EntertainmentActivityEntity handball = entertainmentActivityRepository.findEntertainmentActivityEntityByName("Handball");
        final EntertainmentActivityEntity volley = entertainmentActivityRepository.findEntertainmentActivityEntityByName("Volley");
        final EntertainmentActivityEntity basket = entertainmentActivityRepository.findEntertainmentActivityEntityByName("Basket");

        final UserEntity ownerMihai = new UserEntity();
        final UserDetailsEntity ownerDetailsMihai = new UserDetailsEntity();
        final Set<EntertainmentPlaceEntity> entertainmentPlaceEntities = new HashSet<>();
        final EntertainmentPlaceEntity entertainmentPlaceEntity1 = new EntertainmentPlaceEntity();
        final EntertainmentPlaceEntity entertainmentPlaceEntity2 = new EntertainmentPlaceEntity();
        final AddressEntity addressEntity1 = new AddressEntity();
        final AddressEntity addressEntity2 = new AddressEntity();

        final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity1 = new EntertainmentActivityPlaceEntity();
        final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity2 = new EntertainmentActivityPlaceEntity();
        final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity3 = new EntertainmentActivityPlaceEntity();
        final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity4 = new EntertainmentActivityPlaceEntity();
        final EntertainmentActivityPlaceEntity entertainmentActivityPlaceEntity5 = new EntertainmentActivityPlaceEntity();

        entertainmentActivityPlaceEntity1.setEntertainmentPlace(entertainmentPlaceEntity1);
        entertainmentActivityPlaceEntity1.setEntertainmentActivity(football);

        entertainmentActivityPlaceEntity2.setEntertainmentPlace(entertainmentPlaceEntity1);
        entertainmentActivityPlaceEntity2.setEntertainmentActivity(basket);

        entertainmentActivityPlaceEntity3.setEntertainmentPlace(entertainmentPlaceEntity1);
        entertainmentActivityPlaceEntity3.setEntertainmentActivity(volley);

        entertainmentActivityPlaceEntity4.setEntertainmentPlace(entertainmentPlaceEntity2);
        entertainmentActivityPlaceEntity4.setEntertainmentActivity(basket);

        entertainmentActivityPlaceEntity5.setEntertainmentPlace(entertainmentPlaceEntity2);
        entertainmentActivityPlaceEntity5.setEntertainmentActivity(volley);

        addressEntity1.setEntertainmentPlace(entertainmentPlaceEntity1);
        addressEntity1.setCounty("County1");
        addressEntity1.setCity("City1");
        addressEntity1.setStreet("Street1");
        addressEntity1.setNumber("Number1");

        entertainmentPlaceEntity1.setName("Name1");
        entertainmentPlaceEntity1.setAddress(addressEntity1);
        entertainmentPlaceEntity1.setUserDetails(ownerDetailsMihai);
        entertainmentPlaceEntity1.getEntertainmentActivityPlaceEntities().add(entertainmentActivityPlaceEntity1);
        entertainmentPlaceEntity1.getEntertainmentActivityPlaceEntities().add(entertainmentActivityPlaceEntity2);
        entertainmentPlaceEntity1.getEntertainmentActivityPlaceEntities().add(entertainmentActivityPlaceEntity3);

        addressEntity2.setEntertainmentPlace(entertainmentPlaceEntity2);
        addressEntity2.setCounty("County1");
        addressEntity2.setCity("City1");
        addressEntity2.setStreet("Street1");
        addressEntity2.setNumber("Number1");

        entertainmentPlaceEntity2.setName("Name2");
        entertainmentPlaceEntity2.setAddress(addressEntity2);
        entertainmentPlaceEntity2.setUserDetails(ownerDetailsMihai);
        entertainmentPlaceEntity2.getEntertainmentActivityPlaceEntities().add(entertainmentActivityPlaceEntity4);
        entertainmentPlaceEntity2.getEntertainmentActivityPlaceEntities().add(entertainmentActivityPlaceEntity5);

        entertainmentPlaceEntity1.getEntertainmentActivityPlaceEntities().add(entertainmentActivityPlaceEntity1);
        entertainmentPlaceEntity1.getEntertainmentActivityPlaceEntities().add(entertainmentActivityPlaceEntity2);
        entertainmentPlaceEntity1.getEntertainmentActivityPlaceEntities().add(entertainmentActivityPlaceEntity3);
        entertainmentPlaceEntities.add(entertainmentPlaceEntity1);

        entertainmentPlaceEntity2.getEntertainmentActivityPlaceEntities().add(entertainmentActivityPlaceEntity4);
        entertainmentPlaceEntity2.getEntertainmentActivityPlaceEntities().add(entertainmentActivityPlaceEntity5);
        entertainmentPlaceEntities.add(entertainmentPlaceEntity2);

        ownerMihai.getAuthorities().add(renterRole);
        ownerMihai.getAuthorities().add(ownerRole);
        ownerMihai.setUsername("mihai");
        ownerMihai.setPassword(passwordEncoder.encode("password"));
        ownerMihai.setUserDetails(ownerDetailsMihai);

        ownerDetailsMihai.setUser(ownerMihai);
        ownerDetailsMihai.setEntertainmentPlaces(entertainmentPlaceEntities);

        userRepository.save(ownerMihai);
        userDetailsRepository.save(ownerDetailsMihai);
        entertainmentPlaceRepository.save(entertainmentPlaceEntity1);
        entertainmentPlaceRepository.save(entertainmentPlaceEntity2);
        addressRepository.save(addressEntity1);
        addressRepository.save(addressEntity2);
        entertainmentActivityPlaceRepository.save(entertainmentActivityPlaceEntity1);
        entertainmentActivityPlaceRepository.save(entertainmentActivityPlaceEntity2);
        entertainmentActivityPlaceRepository.save(entertainmentActivityPlaceEntity3);
        entertainmentActivityPlaceRepository.save(entertainmentActivityPlaceEntity4);
        entertainmentActivityPlaceRepository.save(entertainmentActivityPlaceEntity5);
    }

    private void saveOwners() {
        saveOwnerMihai();
    }

    @Override
    public void run(String... args) {
        //TODO I have to ask somebody which type of database (h2 or a real mysql db) should I use when active profile is test or dev
        if (activeProfile.equals("dev") || activeProfile.equals("test")) {
            saveRoles();
            saveEntertainmentActivities();
            saveAdmins();
            saveOwners();
        }
        System.out.println(roleRepository.count() + " roles");
        System.out.println(userRepository.count() + " users");
        System.out.println(userDetailsRepository.count() + " user details");
        System.out.println(entertainmentPlaceRepository.count() + " entertainment places");
        System.out.println(addressRepository.count() + " addresses");
        System.out.println(entertainmentActivityRepository.count() + " entertainment activities");
        System.out.println(entertainmentActivityPlaceRepository.count() + " entertainment places activities");
    }
}
