package com.acme.biz.app.controller;

import com.acme.biz.app.feign.FooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController {

    @Autowired
    private FooService fooService;

    @GetMapping(value = "/foo")
    public String foo() {
        return fooService.foo();
    }
}
