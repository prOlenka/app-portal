package com.intership.app_portal.service;

import com.intership.app_portal.dto.CompanyRequestDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


@Getter
@Setter
public class CompanyService {
    private String CompanyName;
    private String CompanyAddress;
    private String CompanyInn;
    private String CompanyKpp;
    private String CompanyOgrn;
    private String CompanyEmail;
    private String CompanyOwner;
    private String password = PasswordService.generatePassword();

    DaDataService daDataService = new DaDataService();

    KeycloakService keycloakService = new KeycloakService();


    public void createCompany(String inn, String kpp, String companyEmail) {

        try {
            daDataService.getDaData(inn, kpp);
        } catch (JSONException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.CompanyEmail = companyEmail;

        CompanyRequestDTO dto = new CompanyRequestDTO(CompanyName, CompanyAddress, CompanyInn, CompanyKpp, CompanyOgrn, CompanyEmail ,CompanyOwner, password);
        keycloakService.addCompany(dto.builder());
    }

//    public HttpHeaders update(UUID fromString, PortalCompanyRequest request) {
//    }


    public boolean delete(UUID uuid) {
        return false;
    }

}
