package org.example.trackit.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.entity.User;
import org.example.trackit.repository.UserRepository;
import org.example.trackit.security.AppUserDetails;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Cacheable("userDetailsCache")
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("Getting userDetails for: {}", username);
        return new AppUserDetails(user);
    }
}
