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
    public ApplicationRunner applicationRunner(@Autowired EurekaClient eurekaClient) {
        return args -> {
            // serviceWeightMap key: spring application name; value: Map key: ip+port value: weight
            ConcurrentMap<String, ConcurrentMap<String, Integer> > serviceWeightMap = new ConcurrentHashMap<>();

            // 获取服务注册时间
            List<InstanceInfo> infos = eurekaClient.getInstancesByVipAddress("service-provider", false);
            ConcurrentMap<String, Integer> weightMap = serviceWeightMap.computeIfAbsent("service-provider", key -> new ConcurrentHashMap<>());
            infos.forEach(instance -> {
                long registrationTimestamp = instance.getLeaseInfo().getRegistrationTimestamp();
                long uptime = System.currentTimeMillis() - registrationTimestamp;
                long warmup = 10 * 60 * 1000;
                long weight = 1;
                System.out.println(registrationTimestamp);
                System.out.println(uptime);
                int ww = (int) ( uptime / ((float) warmup / weight));
                System.out.println(ww);
                String serviceId = instance.getIPAddr()+":"+instance.getPort();
                weightMap.computeIfAbsent(serviceId, k-> ww);
            });

            System.out.println(serviceWeightMap);
        };
    }

    @Bean
    public RibbonClientSpecification ribbonClientSpecification() {
        RibbonClientSpecification ribbonClientSpecification = new RibbonClientSpecification();
        ribbonClientSpecification.setName("service-provider");
        ribbonClientSpecification.setConfiguration(new Class[]{CustomRibbonClientConfiguration.class});
        return ribbonClientSpecification;
    }
}
