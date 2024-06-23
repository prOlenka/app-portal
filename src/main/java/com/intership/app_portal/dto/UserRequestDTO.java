package com.intership.app_portal.dto;

import com.intership.app_portal.roles.Role;
import lombok.Data;

@Data
public class UserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

}