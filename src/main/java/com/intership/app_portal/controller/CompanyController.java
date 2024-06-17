package com.intership.app_portal.controller;

import com.intership.app_portal.service.CompanyService;
import jakarta.validation.constraints.Email;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("portal/v1/company")

public class CompanyController {
    String INN = "7707083893";
    String KPP = "540602001";

    @GetMapping
    public void createCompany(@Email @RequestParam("email") String email) {
        CompanyService companyService = new CompanyService();
        companyService.createCompany(INN, KPP, email);
    }





}