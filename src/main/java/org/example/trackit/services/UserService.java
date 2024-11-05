package org.example.trackit.services;

import org.example.trackit.dto.LoginDTO;
import org.example.trackit.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    @Transactional
    void registerUser(LoginDTO loginDTO);

    User findUserByUsername(String username);
}
