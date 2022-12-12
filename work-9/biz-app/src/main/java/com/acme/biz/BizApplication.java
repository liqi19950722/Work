package com.acme.biz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class BizApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizApplication.class, args);
    }
}

@RestController
class FooController {
    private static final Logger logger = LoggerFactory.getLogger(FooController.class);

    @GetMapping(value = "/foo")
    public String foo() {
        logger.info("foo");
        return "foo";
    }
}
