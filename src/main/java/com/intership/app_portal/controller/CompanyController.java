package com.intership.app_portal.controller;

import com.intership.app_portal.dto.CompanyRequestDTO;
import com.intership.app_portal.exceptions.ClientNotFoundException;
import com.intership.app_portal.exceptions.KeycloakException;
import com.intership.app_portal.exceptions.UserNotFoundException;
import com.intership.app_portal.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCompany(@RequestBody CompanyRequestDTO companyRequestDTO) {
        try {
            companyService.registerCompany(companyRequestDTO);
            return ResponseEntity.ok("Company registration successful");
        } catch (KeycloakException | UserNotFoundException | ClientNotFoundException | JSONException |
                 UnsupportedEncodingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
