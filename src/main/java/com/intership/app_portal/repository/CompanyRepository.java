package com.intership.app_portal.repository;

import com.intership.app_portal.dto.CompanyRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<CompanyRequestDTO, UUID> {
        Optional<CompanyRequestDTO> findByName(String name);
    }
