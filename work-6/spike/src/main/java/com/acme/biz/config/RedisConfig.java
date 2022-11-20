package com.acme.biz.config;

import com.acme.biz.redis.connection.DelegateRedisConnectionFactory;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, MeterRegistry meterRegistry) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(new DelegateRedisConnectionFactory(redisConnectionFactory, meterRegistry));
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory, MeterRegistry meterRegistry) {
        return new StringRedisTemplate(new DelegateRedisConnectionFactory(redisConnectionFactory, meterRegistry));
    }
}
