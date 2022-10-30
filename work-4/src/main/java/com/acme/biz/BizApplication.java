package com.acme.biz;

import com.acme.biz.servlet.filter.GlobalCircuitBreakerFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerProperties;
import io.github.resilience4j.common.CompositeCustomizer;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class BizApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizApplication.class, args);
    }

    @Bean
    public GlobalCircuitBreakerFilter globalCircuitBreakerFilter(CircuitBreakerRegistry circuitBreakerRegistry) {

        return new GlobalCircuitBreakerFilter(circuitBreakerRegistry);
    }

    @Configuration
    static class Listener implements EnvironmentAware {

        private static final Logger log = LoggerFactory.getLogger(Listener.class);

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private CircuitBreakerRegistry circuitBreakerRegistry;
        @Autowired
        private CompositeCustomizer<CircuitBreakerConfigCustomizer> customizerMap;

        @EventListener(classes = EnvironmentChangeEvent.class)
        public void onApplicationEvent(EnvironmentChangeEvent event) {

            Set<String> keys = event.getKeys();
            log.info("CircuitBreakerProperty Listener listen EnvironmentChangeEvent. changed keys {}", keys);

            keys.forEach(key -> {
                if (key.startsWith("resilience4j.circuitbreaker")) {
                    // 重新绑定一下属性
                    Binder binder = Binder.get(environment);
                    CircuitBreakerProperties circuitBreakerProperties = binder.bindOrCreate("resilience4j.circuitbreaker", CircuitBreakerProperties.class);

                    // 取出instance Name
                    String substring = key.substring("resilience4j.circuitbreaker.instances".length() + 1);
                    String instance = substring.substring(0, substring.indexOf("."));

                    // 找到instance对应的配置
                    Optional<CircuitBreakerConfigurationProperties.InstanceProperties> instanceProperties = circuitBreakerProperties.findCircuitBreakerProperties(instance);
                    instanceProperties.ifPresent(properties -> {
                        try {
                            ObjectMapper copy = objectMapper.copy();
                            copy.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                            if (log.isDebugEnabled()) {
                                log.debug("instance {}, value {}", instance, copy.writerWithDefaultPrettyPrinter().writeValueAsString(properties));
                            }
                        } catch (JsonProcessingException ignore) {

                        }

                        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(instance);
                        if (log.isDebugEnabled()) {
                            log.debug("instance {} state {}", instance, circuitBreaker.getState());
                        }
                        // 重建
                        circuitBreakerRegistry.circuitBreaker(instance, circuitBreakerProperties.createCircuitBreakerConfig(instance, properties, customizerMap));
                    });
                }
            });
        }

        Environment environment;

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }
    }
}

