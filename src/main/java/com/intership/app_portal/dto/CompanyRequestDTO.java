package com.intership.app_portal.dto;


import lombok.Data;



@Data
public class CompanyRequestDTO {

    private String CompanyName;
    private String CompanyAddress;
    private String CompanyInn;
    private String CompanyKpp;
    private String CompanyOgrn;
    private String CompanyOwner;
    private String CompanyEmail;
    private String password;

    public CompanyRequestDTO(String companyName, String companyAddress, String companyInn, String companyKpp, String companyOgrn, String companyEmail, String companyOwner, String password) {
        CompanyName = companyName;
        CompanyAddress = companyAddress;
        CompanyInn = companyInn;
        CompanyKpp = companyKpp;
        CompanyOgrn = companyOgrn;
        CompanyOwner = companyOwner;
        CompanyEmail = companyEmail;
        this.password = password;
    }

    public CompanyRequestDTO builder() {
        CompanyRequestDTO companyRequestDTO = new CompanyRequestDTO(CompanyName, CompanyAddress, CompanyInn, CompanyKpp, CompanyOgrn, CompanyEmail ,CompanyOwner, password);
        companyRequestDTO.setCompanyName(CompanyName);
        companyRequestDTO.setCompanyAddress(CompanyAddress);
        companyRequestDTO.setCompanyInn(CompanyInn);
        companyRequestDTO.setCompanyKpp(CompanyKpp);
        companyRequestDTO.setCompanyOgrn(CompanyOgrn);
        companyRequestDTO.setCompanyOwner(CompanyOwner);
        companyRequestDTO.setCompanyEmail(CompanyEmail);
        companyRequestDTO.setPassword(password);
        return companyRequestDTO;
    }

}
