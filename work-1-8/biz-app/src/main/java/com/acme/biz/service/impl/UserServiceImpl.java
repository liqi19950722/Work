package com.acme.biz.service.impl;

import com.acme.biz.domain.User;
import com.acme.biz.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    private List<User> userList = new ArrayList<>();

    @Override
    public void registerUser(User user) {
        userList.add(user);
    }
}
