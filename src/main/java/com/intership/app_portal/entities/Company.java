package com.intership.app_portal.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "company")
@Data
public class Company {
        @Id
        private UUID id;

        private String companyName;
        private String companyAddress;
        private String companyInn;
        private String companyKpp;
        private String companyOgrn;
        private String companyOwner;
}