package com.intership.app_portal.service;

import org.apache.commons.lang.RandomStringUtils;

public class PasswordService {

    public static String generatePassword(){
        String pass = "";
        for (int i = 0; i < 8; i++) {
            pass = pass + (RandomStringUtils.randomAlphanumeric(6));
        }
        System.out.println(pass);

        return pass;
    }
}
