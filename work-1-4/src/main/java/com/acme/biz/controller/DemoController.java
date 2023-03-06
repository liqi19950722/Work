package com.acme.biz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @GetMapping(value = "/demo")
    public String demo() {
        log.info("info demo");
        log.debug("debug demo");
        return "demo";
    }
}
