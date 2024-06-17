package com.intership.app_portal.dto;

import com.intership.app_portal.roles.Roles;
import lombok.Data;

import java.util.UUID;

@Data
public class UserRequestDTO {
    private String userName;
    private String userPassword;
    private UUID UserId;
    private Roles UserRole;
}
