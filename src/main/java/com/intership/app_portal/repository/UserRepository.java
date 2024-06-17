package com.intership.app_portal.repository;

import com.intership.app_portal.dto.UserRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserRequestDTO, UUID> {
    Optional<UserRequestDTO> findByName(String name);
}
