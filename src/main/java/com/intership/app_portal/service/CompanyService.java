package com.intership.app_portal.service;

import com.intership.app_portal.dto.CompanyRequestDTO;
import com.intership.app_portal.entities.Company;
import com.intership.app_portal.entities.User;
import com.intership.app_portal.exceptions.ClientNotFoundException;
import com.intership.app_portal.exceptions.KeycloakException;
import com.intership.app_portal.exceptions.UserNotFoundException;
import com.intership.app_portal.roles.Role;
import com.intership.app_portal.repository.CompanyRepository;
import com.intership.app_portal.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class CompanyService {

    private KeycloakService keycloakService;


    public void registerCompany(CompanyRequestDTO companyRequestDTO) throws UserNotFoundException, ClientNotFoundException, JSONException, UnsupportedEncodingException, KeycloakException {
        keycloakService.registerCompany(
                companyRequestDTO.getCompanyName(),
                companyRequestDTO.getCompanyInn(),
                companyRequestDTO.getCompanyKpp(),
                companyRequestDTO.getCompanyEmail()
        );
    }
}
