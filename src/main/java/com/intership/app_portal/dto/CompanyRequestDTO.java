// CompanyRequestDTO.java
package com.intership.app_portal.dto;

import lombok.Data;

@Data
public class CompanyRequestDTO {
    private String companyName;
    private String companyAddress;
    private String companyInn;
    private String companyKpp;
    private String companyOgrn;
    private String companyOwner;
    private String companyEmail;
    private String password;
}