package com.intership.app_portal.service;

import com.intership.app_portal.entities.User;
import com.intership.app_portal.repository.UserRepository;
import com.intership.app_portal.roles.Role;
import org.json.JSONException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {

    private final Keycloak keycloak;
    private final DaDataService daDataService;
    private final PasswordService passwordService;
    private final EmailService emailService;
    private UserRepository userRepository;

    @Value("${keycloak.realm}")
    private String realmName;

    @Autowired
    public KeycloakService(Keycloak keycloak,
                           DaDataService daDataService,
                           PasswordService passwordService,
                           EmailService emailService) {
        this.keycloak = keycloak;
        this.daDataService = daDataService;
        this.passwordService = passwordService;
        this.emailService = emailService;
    }

    //REGISTRATOR role
    public void registerUser(String firstName, String lastName, String email) throws Exception {
        RealmResource realm = keycloak.realm(realmName);
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);
        userRepresentation.setEmail(email);
        userRepresentation.setUsername(email);
        userRepresentation.setEnabled(true);

        // Create the user in Keycloak
        Response result = realm.users().create(userRepresentation);
        if (result.getStatus() != Response.Status.CREATED.getStatusCode()) {
            throw new Exception("Failed to create user");
        }

        String userId = getCreatedId(result);

        String password = passwordService.generatePassword();
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        realm.users().get(userId).resetPassword(credential);

        setRole(realm, userId, Role.REGISTRATOR.name());

        emailService.sendSimpleMessage(email, "Your registration is successful", "Your password is: " + password);
    }

    // Register a company with ADMIN role for the registrator
    public void registerCompany(String companyName, String inn, String kpp, String registratorEmail) throws IOException, JSONException {
        RealmResource realm = keycloak.realm(realmName);
        User registrator = userRepository.findByEmail(registratorEmail).orElseThrow(() -> new IOException("Registrator not found"));
        if (!registrator.getUserRole().equals(Role.REGISTRATOR)) {
            throw new IOException("Only registrators can register companies");
        }

        if (daDataService.getDaData(inn, kpp)) {
            //Keycloak
            ClientRepresentation clientRepresentation = new ClientRepresentation();
            clientRepresentation.setClientId(companyName);

            Response response = realm.clients().create(clientRepresentation);
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new IOException("Failed to create company");
            }

            //ADMIN role to registrator
            ClientResource clientResource = getClientResourceById(realm, companyName);
            String userId = getUserIdByUserName(realm, registratorEmail);
            String clientId = getCreatedId(response);
            addClientRole(realm, userId, clientResource, clientId, Role.ADMIN.name(), companyName);
        }
    }

    public void addEmployee(String companyName, String email, String role, String adminEmail) throws IOException {
        RealmResource realm = keycloak.realm(realmName);
        User admin = userRepository.findByEmail(adminEmail).orElseThrow(() -> new IOException("Admin not found"));
        if (!admin.getUserRole().equals(Role.ADMIN)) {
            throw new IOException("Only admins of the company can add employees");
        }

        UserRepresentation userRepresentation = getUserRepresentationByUsername(realm, email);
        String userId = userRepresentation.getId();
        ClientResource clientResource = getClientResourceById(realm, companyName);
        String clientId = getClientIdByName(realm, companyName);
        addClientRole(realm, userId, clientResource, clientId, role, companyName);
    }

    // Set role for a user
    private void setRole(RealmResource realm, String userId, String role){
        RoleResource roleResource = realm.roles().get(role);
        RoleRepresentation roleRepresentation = roleResource.toRepresentation();
        realm.users().get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    // Generate new password for user
    public void generateNewPassword(String email) throws IOException {
        RealmResource realm = keycloak.realm(realmName);
        UserResource userResource = getUserResourceByEmail(realm, email);
        String newPassword = passwordService.generatePassword();
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPassword);
        userResource.resetPassword(credential);
        emailService.sendSimpleMessage(email, "New password", "Your new password is: " + newPassword);
    }

    public void updateUser(String email, String firstName, String lastName, String newEmail) throws IOException {
        RealmResource realm = keycloak.realm(realmName);
        UserResource userResource = getUserResourceByEmail(realm, email);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);
        userRepresentation.setEmail(newEmail);
        userResource.update(userRepresentation);
    }

    private UserResource getUserResourceByEmail(RealmResource realm, String email) throws IOException {
        UserRepresentation userRepresentation = realm.users().search(email).stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IOException("User not found"));
        return realm.users().get(userRepresentation.getId());
    }

    // Helper methods
    private String getCreatedId(Response response) throws IOException {
        URI location = response.getLocation();
        if (location == null) {
            throw new IOException("Location header is missing in response");
        }
        return location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
    }

    private ClientResource getClientResourceById(RealmResource realm, String clientId) throws IOException {
        ClientRepresentation clientRepresentation = realm.clients().findByClientId(clientId).stream()
                .findFirst()
                .orElseThrow(() -> new IOException("Client not found"));
        return realm.clients().get(clientRepresentation.getId());
    }

    private String getClientIdByName(RealmResource realm, String companyName) throws IOException {
        ClientRepresentation clientRepresentation = realm.clients().findByClientId(companyName).stream()
                .findFirst()
                .orElseThrow(() -> new IOException("Client not found"));
        return clientRepresentation.getId();
    }

    private UserRepresentation getUserRepresentationByUsername(RealmResource realm, String username) throws IOException {
        return realm.users().searchByUsername(username, true).stream()
                .findFirst()
                .orElseThrow(() -> new IOException("User not found"));
    }

    private String getUserIdByUserName(RealmResource realm, String username) throws IOException {
        return getUserRepresentationByUsername(realm, username).getId();
    }

    private void addClientRole(RealmResource realm, String userId, ClientResource clientResource, String clientId, String role, String companyName) throws IOException {
        UserResource userResource = getUserResourceByEmail(realm, userId);
        RoleRepresentation roleRepresentation = clientResource.roles().get(role).toRepresentation();
        userResource.roles().clientLevel(clientId).add(Collections.singletonList(roleRepresentation));
        UserRepresentation userRepresentation = userResource.toRepresentation();
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(companyName, Collections.singletonList(role));
        userRepresentation.setAttributes(attributes);
        userResource.update(userRepresentation);
    }
}
