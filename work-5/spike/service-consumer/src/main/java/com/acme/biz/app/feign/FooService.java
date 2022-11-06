package com.acme.biz.app.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service-provider")
public interface FooService {

    @GetMapping(value = "/foo")
    String foo();
}
