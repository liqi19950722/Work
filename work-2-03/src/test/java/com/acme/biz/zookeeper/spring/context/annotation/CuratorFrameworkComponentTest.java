package com.acme.biz.zookeeper.spring.context.annotation;

import com.acme.biz.zookeeper.spring.context.TestConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig
class CuratorFrameworkComponentTest extends TestConfiguration {

    @Autowired
    ApplicationContext applicationContext;

    @BeforeAll
    public static void init() throws Exception {
        zookeeperStart();
//        createCuratorFramework();
    }


    @Test
    public void should_get_curator_framework_bean_form_application_context() {

        CuratorFramework component = applicationContext.getBean(CuratorFramework.class);
        assertNotNull(component);
    }

    @AfterAll
    public static void destroy() {
//        getCuratorFramework().close();
        zookeeperClose();
    }
}
