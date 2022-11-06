package com.acme.biz.app;

import com.acme.biz.app.configuration.netflix.ribbon.CustomRibbonClientConfiguration;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.converters.Auto;
import com.netflix.discovery.shared.Applications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClientSpecification;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class ServiceConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceConsumerApplication.class, args);
    }

    @Bean
    public RibbonClientSpecification ribbonClientSpecification() {
        RibbonClientSpecification ribbonClientSpecification = new RibbonClientSpecification();
        ribbonClientSpecification.setName("service-provider");
        ribbonClientSpecification.setConfiguration(new Class[]{CustomRibbonClientConfiguration.class});
        return ribbonClientSpecification;
    }
}
