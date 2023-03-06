package com.acme.biz.web.controller;

import com.acme.biz.api.ApiRequest;
import com.acme.biz.api.ApiResponse;
import com.acme.biz.api.interfaces.UserRegistrationRestService;
import com.acme.biz.api.model.User;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRegistrationRestController implements UserRegistrationRestService {
    @Override
    public ApiResponse<Boolean> registerUser(User user) {
        return ApiResponse.ok(Boolean.TRUE);
    }

    @Override
    public ApiResponse<Boolean> registerUser(ApiRequest<User> userRequest) {
        return ApiResponse.ok(Boolean.TRUE);
    }
}
