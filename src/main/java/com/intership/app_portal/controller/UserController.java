package com.intership.app_portal.controller;

import com.intership.app_portal.dto.UserRequestDTO;
import com.intership.app_portal.entities.User;
import com.intership.app_portal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        userService.registerUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<String> changePassword(@PathVariable String userId, @RequestParam String newPassword) {
        userService.changePassword(userId, newPassword);
        return ResponseEntity.ok("Password changed successfully");
    }
}