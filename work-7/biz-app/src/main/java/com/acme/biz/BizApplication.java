package com.acme.biz;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class BizApplication {

    public static void main(String[] args) {
        SpringApplication.run(BizApplication.class, args);
    }
}

@RestController
class DemoController {

    @Autowired
    private MeterRegistry registry;

    @GetMapping(value = "/demo")
    public void demo() {
        Timer.Sample sample = Timer.start(registry);

        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt(10));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            sample.stop(Timer.builder("uri.demo")
                    .description("测试demo")
                    .tags("requestUri", "/demo")
                    .register(registry));
        }

    }

}
