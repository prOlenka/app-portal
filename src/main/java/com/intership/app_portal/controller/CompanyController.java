package com.intership.app_portal.controller;

import com.intership.app_portal.dto.CompanyRequestDTO;
import com.intership.app_portal.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
@RequestMapping("/portal/v1/company/")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCompany(@RequestBody CompanyRequestDTO companyRequestDTO) throws IOException {
        try {
            companyService.registerCompany(companyRequestDTO);
            return ResponseEntity.ok("Company registration successful");
        } catch ( JSONException | UnsupportedEncodingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCompany(@RequestBody CompanyRequestDTO companyRequestDTO){
        companyService.updateCompany(companyRequestDTO);
        return ResponseEntity.ok("Company update successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable UUID id){
        companyService.deleteCompany(id);
        return ResponseEntity.ok("Company delete successful");
    }
}
