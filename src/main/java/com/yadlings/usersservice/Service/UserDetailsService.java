package com.yadlings.usersservice.Service;

import com.yadlings.usersservice.Exceptions.UserExceptions;
import com.yadlings.usersservice.Repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Log4j2
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    protected final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
        log.info("User Detail Service Initialized");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserExceptions.UserNotFound(String.format("%s does not exist",username)));
    }
}
