package com.acme.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class BizApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizApplication.class, args);
    }
}

@RestController
class FooController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping(value = "/redis/set/{key}")
    public void setValue(@PathVariable(value = "key") String key, @RequestParam("value") String value) {
        redisTemplate.opsForValue().set(key, value);
    }

}
