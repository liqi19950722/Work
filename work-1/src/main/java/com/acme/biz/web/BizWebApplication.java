package com.acme.biz.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BizWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BizWebApplication.class, args);
    }

    // client api definition
    // version   url                    request                 response
    // v1        /api/user/register/v1  ApiRequest<User>        ApiResponse<Boolean>
    // v2        /api/user/register/v2  User                    ApiResponse<Boolean>
    // v3        /api/user/register/v3  User                    Boolean

    // server api provider
    // v3        /api/user/register/v3  User                    Boolean
}
