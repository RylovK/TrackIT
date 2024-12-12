package org.example.trackit.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.mapper.UserMapper;
import org.example.trackit.dto.LoginDTO;
import org.example.trackit.entity.User;
import org.example.trackit.entity.properties.Role;
import org.example.trackit.repository.UserRepository;
import org.example.trackit.services.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    @CacheEvict(value = "userDetailsCache", allEntries = true)
    @Override
    public void registerUser(LoginDTO loginDTO) {
        User user = userMapper.toUser(loginDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        log.info("User \"{}\" registered successfully", user.getUsername());
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
