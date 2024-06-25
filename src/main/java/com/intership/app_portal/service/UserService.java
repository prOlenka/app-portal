package com.intership.app_portal.service;

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
    private final PasswordService passwordService;

    public void registerUser(String firstName, String lastName, String email) throws Exception {
        keycloakService.registerUser(firstName, lastName, email);
    }

    public void updateUser(String email, String firstName, String lastName, String newEmail) throws IOException {
        keycloakService.updateUser(email, firstName, lastName, newEmail);
    }

    public void generateNewPassword(String email) throws IOException {
        keycloakService.generateNewPassword(email);
    }
}
