package org.example.trackit.services.impl;

import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.UserMapper;
import org.example.trackit.dto.LoginDTO;
import org.example.trackit.entity.User;
import org.example.trackit.entity.properties.Role;
import org.example.trackit.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public void registerUser(LoginDTO loginDTO) {
        User user = userMapper.toUser(loginDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
