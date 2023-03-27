package com.acme.biz.zookeeper.spring.context.annotation;

import com.acme.biz.zookeeper.spring.beans.support.TimeUnitEditorRegister;
import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.Test;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = {
        ZookeeperComponentConfiguration.class,
//        CuratorFrameworkComponentTest.class
})
@TestPropertySource(properties = {
        "zookeeper.connectString=localhost:2181",
        "zookeeper.sessionTimeoutMs=60000",
        "zookeeper.connectionTimeoutMs=15000",
        "zookeeper.blockUntilConnected=10",
        "zookeeper.baseSleepTimeMs=50",
        "zookeeper.maxRetries=10",
        "zookeeper.maxSleepMs=500",
        "zookeeper.blockUntilConnectedTimeUnit=SECONDS"
})
public class CuratorFrameworkComponentTest {

    @Bean
    public CustomEditorConfigurer CustomEditorConfigurer() {
        CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
        customEditorConfigurer.setPropertyEditorRegistrars(new PropertyEditorRegistrar[]{new TimeUnitEditorRegister()});
        return customEditorConfigurer;
    }

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void should_get_curator_framework_bean_form_application_context() {

        CuratorFramework component = applicationContext.getBean(CuratorFramework.class);
        assertNotNull(component);
    }
}
