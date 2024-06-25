package com.intership.app_portal.service;

import com.intership.app_portal.dto.CompanyRequestDTO;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class CompanyService {

    private KeycloakService keycloakService;


    public void registerCompany(CompanyRequestDTO companyRequestDTO) throws JSONException, IOException {
        keycloakService.registerCompany(
                companyRequestDTO.getCompanyName(),
                companyRequestDTO.getCompanyInn(),
                companyRequestDTO.getCompanyKpp(),
                companyRequestDTO.getCompanyEmail()
        );
    }
}
