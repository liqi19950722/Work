package com.acme.biz.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController {

    @GetMapping(value = "/foo")
    public String foo() {
        return "foo";
    }
}
