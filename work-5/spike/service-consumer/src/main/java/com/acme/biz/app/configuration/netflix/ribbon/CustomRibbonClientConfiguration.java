package com.acme.biz.app.configuration.netflix.ribbon;

import com.acme.biz.app.loadbalancer.RoundRobinBasedUptimeRule;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;

public class CustomRibbonClientConfiguration {

    @Bean
    public IRule ribbonRule(IClientConfig config) {
        RoundRobinBasedUptimeRule rule = new RoundRobinBasedUptimeRule();
//        rule.initWithNiwsConfig(config);
        return rule;
    }
}
