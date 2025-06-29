package com.property.service;

import com.property.dto.RegisterRequest;
import com.property.exception.AppException;
import com.property.model.User;
import com.property.model.User.Role;
import com.property.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public void registerUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.valueOf(registerRequest.getRole()));
        user.setResidentId(registerRequest.getResidentId());

        userRepository.save(user);
    }

    public boolean hasAnyAdmin() {
        return userRepository.existsByRole(User.Role.ADMIN);
    }
}