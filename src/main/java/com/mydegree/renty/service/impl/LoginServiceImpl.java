package com.mydegree.renty.service.impl;

import com.mydegree.renty.dao.repository.IUserRepository;
import com.mydegree.renty.service.abstracts.ILoginService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements ILoginService {

    private final IUserRepository userRepository;

    public LoginServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }
}
