package com.acme.biz.app;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import org.checkerframework.checker.units.qual.A;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.netflix.eureka.*;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.cloud.util.ProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import static org.springframework.cloud.netflix.eureka.EurekaClientConfigBean.DEFAULT_ZONE;

@EnableEurekaClient
@SpringBootApplication
public class ServiceProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceProviderApplication.class, args);
    }

   /* @EventListener(value = InstancePreRegisteredEvent.class)
    public void onInstancePreRegisteredEvent(InstancePreRegisteredEvent event) {
        Registration registration = event.getRegistration();
        Map<String, String> metadata = registration.getMetadata();
        metadata.put("startTime", getStartTime());
    }*/

    private static String getStartTime() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return String.valueOf(runtimeMXBean.getStartTime());
    }

    @Bean
    public ApplicationRunner applicationRunner(@Autowired ApplicationInfoManager applicationInfoManager) {
        return args -> {
            InstanceInfo info = applicationInfoManager.getInfo();
            info.getMetadata().put("startTime", getStartTime());
            info.setIsDirty();
        };
    }

    @Bean
    public ApplicationRunner applicationRunner_2(@Autowired EurekaServiceRegistry registry,
                                                 @Autowired EurekaClientConfig eurekaClientConfig,
                                                 @Autowired CloudEurekaInstanceConfig instanceConfig,
                                                 @Autowired ApplicationInfoManager applicationInfoManager,
                                                 @Autowired ApplicationContext applicationContext,
                                                 @Autowired(
            required = false) ObjectProvider<HealthCheckHandler> healthCheckHandler) {

        return (args) -> {


            CloudEurekaClient cloudEurekaClient = new CloudEurekaClient(applicationInfoManager,
                    eurekaClientConfig, applicationContext);
            EurekaRegistration registration = EurekaRegistration.builder(instanceConfig).with(applicationInfoManager)
                    .with(cloudEurekaClient).with(healthCheckHandler).build();
            registry.register(registration);



            EurekaClientConfigBean another = new EurekaClientConfigBean();
            BeanUtils.copyProperties(eurekaClientConfig,another);
            another.getServiceUrl().put(DEFAULT_ZONE, "http://localhost:8762/eureka/");

            InstanceInfo instanceInfo = new InstanceInfoFactory().create(instanceConfig);
            ApplicationInfoManager anotherApplicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);

            CloudEurekaClient anotherCloudEurekaClient = new CloudEurekaClient(anotherApplicationInfoManager,
                    eurekaClientConfig, applicationContext);

            EurekaRegistration anotherRegistration = EurekaRegistration.builder(instanceConfig).with(anotherApplicationInfoManager)
                    .with(anotherCloudEurekaClient).with(healthCheckHandler).build();
            registry.register(anotherRegistration);
        };
    }
}
