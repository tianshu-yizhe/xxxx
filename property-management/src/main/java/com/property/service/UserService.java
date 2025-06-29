package com.property.service;

import com.property.model.User;
import com.property.repository.UserRepository;
import com.property.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean hasAnyAdmin() {
        return userRepository.existsByRole(User.Role.ADMIN);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public User getCurrentUserProfile() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}