package com.intership.app_portal.service;

import com.intership.app_portal.dto.CompanyRequestDTO;
import com.intership.app_portal.dto.UserRequestDTO;
import com.intership.app_portal.roles.Roles;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;


    public void addUser(UserRequestDTO dto) {
        String username = dto.getUserName();
        CredentialRepresentation credential = createPasswordCredentials(dto.getUserPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        UsersResource usersResource = getUsersResource();
        usersResource.create(user);
        addRealmRoleToUser(username, dto.getUserRole().name());
    }

    public void addCompany(CompanyRequestDTO dto) {
        String companyName = dto.getCompanyName();
        CredentialRepresentation credential = createPasswordCredentials(dto.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(companyName);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        UsersResource usersResource = getUsersResource();
        usersResource.create(user);
        addRealmRoleToCompany(companyName, Roles.REGISTRATOR.name());
    }

    private void addRealmRoleToCompany(String companyName, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);
        List<UserRepresentation> users = realmResource.users().search(companyName);
        UserResource userResource = realmResource.users().get(users.get(0).getId());
        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
        RoleMappingResource roleMappingResource = userResource.roles();
        roleMappingResource.realmLevel().add(Collections.singletonList(role));
    }

    private void addRealmRoleToUser(String userName, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);
        List<UserRepresentation> users = realmResource.users().search(userName);
        UserResource userResource = realmResource.users().get(users.get(0).getId());
        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
        RoleMappingResource roleMappingResource = userResource.roles();
        roleMappingResource.realmLevel().add(Collections.singletonList(role));
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }




}