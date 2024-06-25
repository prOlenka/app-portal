package com.intership.app_portal.service;

import com.intership.app_portal.entities.User;
import com.intership.app_portal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakService keycloakService;

    public void registerUser(String firstName, String lastName, String email) throws Exception {
        keycloakService.registerUser(firstName, lastName, email);
    }

    public void updateUser(String email, String firstName, String lastName, String newEmail) throws IOException {
        keycloakService.updateUser(email, firstName, lastName, newEmail);
    }

    public void generateNewPassword(String email) throws IOException {
        keycloakService.generateNewPassword(email);
    }

    public void findByLogin(String email) throws Exception {
        keycloakService.findByEmail(email);
    }
}
