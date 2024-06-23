package com.intership.app_portal.entities;

import com.intership.app_portal.roles.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user")
@Data
public class User {

        @Id
        private UUID id;
        private UUID companyId;
        private String userName;
        private String userPassword;
        private Role userRole;
        private String email;
}