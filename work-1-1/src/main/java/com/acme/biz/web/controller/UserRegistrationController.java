package com.acme.biz.web.controller;

import com.acme.biz.api.exception.UserException;
import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.api.model.User;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRegistrationController implements UserRegistrationService {


    @Override
    public Boolean registerUser(User user) throws UserException {
        return Boolean.TRUE;
    }
}
