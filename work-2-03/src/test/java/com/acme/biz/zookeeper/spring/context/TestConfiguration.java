package com.acme.biz.zookeeper.spring.context;

import com.acme.biz.zookeeper.ZookeeperContainerEnv;
import com.acme.biz.zookeeper.spring.beans.support.TimeUnitEditorRegister;
import com.acme.biz.zookeeper.spring.context.annotation.ZookeeperComponentConfiguration;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@TestPropertySource(properties = {
        "zookeeper.connectString=localhost:2181",
        "zookeeper.sessionTimeoutMs=60000",
        "zookeeper.connectionTimeoutMs=15000",
        "zookeeper.blockUntilConnected=10",
        "zookeeper.baseSleepTimeMs=50",
        "zookeeper.maxRetries=10",
        "zookeeper.maxSleepMs=500",
        "zookeeper.blockUntilConnectedTimeUnit=TimeUnit.SECONDS"
})
@SpringJUnitConfig(classes = {
        ZookeeperComponentConfiguration.class,
        TestConfiguration.BeanConfig.class
})
public class TestConfiguration extends ZookeeperContainerEnv{

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("zookeeper.connectString", ZookeeperContainerEnv::getConnectString);
    }


    @Configuration(proxyBeanMethods = false)
    static class BeanConfig {
        @Bean
        public CustomEditorConfigurer CustomEditorConfigurer() {
            CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
            customEditorConfigurer.setPropertyEditorRegistrars(new PropertyEditorRegistrar[]{new TimeUnitEditorRegister()});
            return customEditorConfigurer;
        }

    }
}
