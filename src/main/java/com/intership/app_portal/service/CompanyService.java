package com.intership.app_portal.service;

import com.intership.app_portal.dto.CompanyRequestDTO;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

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

    public void updateCompany(CompanyRequestDTO companyRequestDTO) {

        try {
            keycloakService.updateCompany(
                    companyRequestDTO.getCompanyName(),
                    companyRequestDTO.getCompanyAddress(),
                    companyRequestDTO.getCompanyInn(),
                    companyRequestDTO.getCompanyKpp(),
                    companyRequestDTO.getCompanyOgrn(),
                    companyRequestDTO.getCompanyOwner(),
                    companyRequestDTO.getCompanyEmail(),
                    companyRequestDTO.getPassword()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCompany(UUID id) {
        try {
            keycloakService.deleteCompany(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
