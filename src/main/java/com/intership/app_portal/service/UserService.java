package com.intership.app_portal.service;

import com.intership.app_portal.dto.CompanyRequestDTO;
import com.intership.app_portal.dto.UserRequestDTO;
import com.intership.app_portal.exceptions.KeycloakException;
import com.intership.app_portal.exceptions.UserNotFoundException;
import com.intership.app_portal.roles.Role;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakService keycloakService;
    private final PasswordService passwordService;

    public void registerUser(String firstName, String lastName, String email) throws KeycloakException, UserNotFoundException {
        keycloakService.registerUser(firstName, lastName, email);
    }

    public void updateUser(String email, String firstName, String lastName, String newEmail) throws UserNotFoundException {
        keycloakService.updateUser(email, firstName, lastName, newEmail);
    }

    public void generateNewPassword(String email) throws UserNotFoundException {
        keycloakService.generateNewPassword(email);
    }
}
