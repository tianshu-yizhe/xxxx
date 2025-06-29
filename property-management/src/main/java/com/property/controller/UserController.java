package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.User;
import com.property.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/check-admin")
    public ResponseEntity<Boolean> hasAnyAdmin() {
        boolean hasAdmin = userService.hasAnyAdmin();
        return ResponseEntity.ok(hasAdmin);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> usernameExists(@RequestParam String username) {
        boolean exists = userService.usernameExists(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESIDENT')")
    public ResponseEntity<User> getCurrentUserProfile() {
        User user = userService.getCurrentUserProfile();
        return ResponseEntity.ok(user);
    }
}