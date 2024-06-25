package com.intership.app_portal.controller;

import com.intership.app_portal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email) {
        try {
            userService.registerUser(firstName, lastName, email);
            return ResponseEntity.ok("User registration successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateUser(
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String newEmail) {
        try {
            userService.updateUser(email, firstName, lastName, newEmail);
            return ResponseEntity.ok("User update successful");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email) {
        try {
            userService.generateNewPassword(email);
            return ResponseEntity.ok("Password reset successful");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}