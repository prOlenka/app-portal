package com.intership.app_portal.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Entity
@Table(name = "company")
@Data
public class CompanyEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        private String CompanyName;
        private String CompanyAddress;
        private String CompanyInn;
        private String CompanyKpp;
        private String CompanyOgrn;
        private String CompanyOwner;

        @ManyToMany(cascade = CascadeType.ALL)
        @JoinTable(name = "company_user",
                joinColumns = @JoinColumn(name="company_id", referencedColumnName="id"),
                inverseJoinColumns = @JoinColumn(name ="user_id", referencedColumnName="id"))
        private Set<UserEntity> tasks = new HashSet<UserEntity>();
    }
