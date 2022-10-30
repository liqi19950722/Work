package org.example;

import org.example.filter.DemoWebFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class WebFluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebFluxApplication.class, args);
    }

    @Bean
    public WebFilter demoWebFilter() {
        return new DemoWebFilter();
    }
}

@RestController
class DemoController {
    @GetMapping(value = "/hello-world")
    public Mono<String> getHelloWorld() {
        return Mono.just("hello world!");
    }

    @PostMapping(value = "/hello-world")
    public Mono<String> postHelloWorld() {
        return Mono.just("hello world!");
    }
}