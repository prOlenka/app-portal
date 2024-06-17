package com.intership.app_portal.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "user")
@Data
public class UserEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        private String username;
        private String userPassword;
        private String userRole;


        @ManyToMany(cascade = CascadeType.ALL)
        @JoinTable(name = "company_user",
                joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
                inverseJoinColumns = @JoinColumn(name = "company_id", referencedColumnName="id"))
        private Set<CompanyEntity> tasks = new HashSet<CompanyEntity>();
}
