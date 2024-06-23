package com.intership.app_portal.service;

import com.intership.app_portal.dto.CompanyRequestDTO;
import com.intership.app_portal.dto.UserRequestDTO;
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

    public void registerCompany(CompanyRequestDTO companyRequestDTO) throws JSONException {
        keycloakService.addCompany(companyRequestDTO);
    }

    public void addEmployee(String adminUserId, UserRequestDTO employeeDTO, Role role) {
        keycloakService.addUser(employeeDTO);
        // Additional logic to link the employee to the company
    }

    public String registerUser(UserRequestDTO userRequestDTO) {
        String password = passwordService.generatePassword();
        userRequestDTO.setPassword(password);
        keycloakService.addUser(userRequestDTO);
        return password;
    }

    public void changePassword(String userId, String newPassword) {
        keycloakService.changePassword(userId, newPassword);
    }

    public void updateUserData(String userId, String firstName, String lastName, String email) {
        keycloakService.updateUserData(userId, firstName, lastName, email);
    }

    public Mono<UserRepresentation> findByEmail(String email) {
        return keycloakService.findByEmail(email);
    }
}
