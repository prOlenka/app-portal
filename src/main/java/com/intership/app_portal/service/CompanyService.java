package com.intership.app_portal.service;

import com.intership.app_portal.entities.Company;
import com.intership.app_portal.entities.User;
import com.intership.app_portal.roles.Role;
import com.intership.app_portal.repository.CompanyRepository;
import com.intership.app_portal.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;


    @Transactional
    public Company registerCompany(String name, String inn, String address, String kpp, String ogrn, User registrator) {
        Company company = new Company();
        company.setCompanyName(name);
        company.setCompanyInn(inn);
        company.setCompanyAddress(address);
        company.setCompanyKpp(kpp);
        company.setCompanyOgrn(ogrn);

        Company savedCompany = companyRepository.save(company);

        registrator.setUserRole(Role.ADMIN);
        registrator.setCompanyId(savedCompany.getId());
        userRepository.save(registrator);

        return savedCompany;
    }


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
