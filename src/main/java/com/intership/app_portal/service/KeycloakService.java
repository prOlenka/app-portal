package com.intership.app_portal.service;

import com.intership.app_portal.dto.CompanyRequestDTO;
import com.intership.app_portal.dto.UserRequestDTO;
import com.intership.app_portal.entities.Company;
import com.intership.app_portal.repository.CompanyRepository;
import com.intership.app_portal.roles.Role;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
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
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;
    private final DaDataService daDataService;
    private final CompanyRepository companyRepository;

    @Value("${keycloak.realm}")
    private String realm;

    public void addUser(UserRequestDTO dto) {
        String username = dto.getFirstName() + " " + dto.getLastName();
        CredentialRepresentation credential = createPasswordCredentials(dto.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(dto.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        UsersResource usersResource = getUsersResource();
        usersResource.create(user);
        addRealmRoleToUser(username, dto.getRole().name());
    }

    public void addCompany(CompanyRequestDTO dto) throws JSONException {
        String companyName = dto.getCompanyName();
        CredentialRepresentation credential = createPasswordCredentials(dto.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(companyName);
        user.setEmail(dto.getCompanyEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        UsersResource usersResource = getUsersResource();
        usersResource.create(user);
        addRealmRoleToCompany(companyName, Role.ADMIN.name());

        JSONObject companyData = daDataService.getDaData(dto.getCompanyInn(), dto.getCompanyKpp());
        saveCompanyData(companyData);
    }

    private void saveCompanyData(JSONObject companyData) throws JSONException {
        Company company = new Company();
        company.setCompanyName(companyData.getString("name"));
        company.setCompanyInn(companyData.getString("inn"));
        company.setCompanyAddress(companyData.getString("address"));
        company.setCompanyKpp(companyData.getString("kpp"));
        company.setCompanyOgrn(companyData.getString("ogrn"));

        companyRepository.save(company);
    }

    public void changePassword(String userId, String newPassword) {
        CredentialRepresentation credential = createPasswordCredentials(newPassword);
        UserResource userResource = getUserResource(userId);
        userResource.resetPassword(credential);
    }

    public void updateUserData(String userId, String firstName, String lastName, String email) {
        UserResource userResource = getUserResource(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);
        userRepresentation.setEmail(email);
        userResource.update(userRepresentation);
    }

    public Mono<UserRepresentation> findByEmail(String email) {
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> users = usersResource.search(email, true);
        if (users.isEmpty()) {
            return Mono.empty();
        }
        return Mono.just(users.get(0));
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

    private UserResource getUserResource(String userId) {
        return keycloak.realm(realm).users().get(userId);
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
