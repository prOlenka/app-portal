package com.intership.app_portal.service;

import com.intership.app_portal.entities.User;
import com.intership.app_portal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakService keycloakService;
    private final UserRepository userRepository;

    public void registerUser(String firstName, String lastName, String email) throws Exception {
        keycloakService.registerUser(firstName, lastName, email);
    }

    public void updateUser(String email, String firstName, String lastName, String newEmail) throws IOException {
        keycloakService.updateUser(email, firstName, lastName, newEmail);
    }

    public void generateNewPassword(String email) throws IOException {
        keycloakService.generateNewPassword(email);
    }

    public User findByLogin(String login) throws Exception {
        // Attempt to find the user by email
        User user = userRepository.findByEmail(login).orElse(null);

        // If the user is not found by email, try finding by username
        if (user == null) {
            user = userRepository.findByUsername(login).orElseThrow(() -> new Exception("User not found"));
        }

        return user;
    }
}
