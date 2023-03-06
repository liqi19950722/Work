package com.acme.biz.controller;

import com.acme.biz.domain.User;
import com.acme.biz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    public void register(@RequestBody User user) {

        userService.registerUser(user);
    }
}
