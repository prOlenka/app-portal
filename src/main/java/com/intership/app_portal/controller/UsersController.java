package com.intership.app_portal.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("portal/v1")

public class UsersController {



}



//                    requests.pathMatchers(HttpMethod.POST, "/portal/v1/user").permitAll();
//// Восстановление пароля
//                    requests.pathMatchers(HttpMethod.GET, "/portal/v1/password/reset-request/{login}").permitAll();
//                    requests.pathMatchers(HttpMethod.POST, "/portal/v1/password").permitAll();
//// Смена пароля
//                    requests.pathMatchers(HttpMethod.PUT, "/portal/v1/password").authenticated();
//        requests.pathMatchers ("/portal/v1/**").hasAnyAuthority (Authority.LOGIST.name(), Authority.ADMIN.name(), Authority. REGISTRATOR.name());