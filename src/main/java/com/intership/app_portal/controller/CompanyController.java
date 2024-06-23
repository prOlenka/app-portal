package com.intership.app_portal.controller;

import com.intership.app_portal.dto.CompanyRequestDTO;
import com.intership.app_portal.dto.UserRequestDTO;
import com.intership.app_portal.entities.Company;
import com.intership.app_portal.entities.User;
import com.intership.app_portal.roles.Role;
import com.intership.app_portal.service.CompanyService;
import com.intership.app_portal.service.KeycloakService;
import com.intership.app_portal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final UserService userService;
    private final CompanyService companyService;
    private final KeycloakService keycloakService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCompany(@RequestBody CompanyRequestDTO companyRequestDTO) throws JSONException {
        keycloakService.addCompany(companyRequestDTO);
        return ResponseEntity.ok("Company registered successfully");
    }

    @PostMapping("/add-employee")
    public ResponseEntity<String> addEmployee(
            @RequestParam String adminUserId,
            @RequestBody UserRequestDTO employeeDTO,
            @RequestParam Role role) {
        try {
            userService.addEmployee(adminUserId, employeeDTO, role);
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add employee");
        }
    }

    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            String password = userService.registerUser(userRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully. Password: " + password);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String userId,
            @RequestParam String newPassword) {
        try {
            userService.changePassword(userId, newPassword);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to change password");
        }
    }

    @PutMapping("/update-user")
    public ResponseEntity<String> updateUserData(
            @RequestParam String userId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email) {
        try {
            userService.updateUserData(userId, firstName, lastName, email);
            return ResponseEntity.ok("User data updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user data");
        }
    }

    @GetMapping("/find-by-email")
    public ResponseEntity<Mono<UserRepresentation>> findUserByEmail(@RequestParam String email) {
        try {
            Mono<UserRepresentation> user = userService.findByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
