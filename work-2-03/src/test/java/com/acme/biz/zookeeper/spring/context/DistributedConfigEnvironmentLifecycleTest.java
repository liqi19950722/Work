package com.acme.biz.zookeeper.spring.context;

import com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase;
import com.acme.biz.zookeeper.distributedconfig.zookeeper.ZookeeperDistributedConfigDataBase;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = {
        DistributedConfigEnvironmentLifecycleTest.class,
        DistributedConfigEnvironmentLifecycle.class
})
class DistributedConfigEnvironmentLifecycleTest extends TestConfiguration {

    @Bean
    public DistributedConfigDataBase zookeeperDistributedConfigDataBase(@Autowired CuratorFramework curatorFramework) {
        return new ZookeeperDistributedConfigDataBase(curatorFramework);
    }
    @Autowired
    private Environment environment;
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeAll
    public static void init() throws Exception {
        zookeeperStart();
        createCuratorFramework();

        getCuratorFramework().create().creatingParentsIfNeeded().forPath("/distributed-config/application/default/distributed/config/test", SerializationUtils.serialize("test"));
    }


    @Test
    public void should_set_property_from_distributed_config_via_spring_lifecycle() {
        DistributedConfigEnvironmentLifecycle bean = applicationContext.getBean(DistributedConfigEnvironmentLifecycle.class);
        assertNotNull(bean);

        // /distributed-config/application/default/distributed/config/test
        String property = environment.getProperty("distributed.config.test");
        assertNotNull(property);
        assertEquals("test", property);
    }

    @AfterAll
    public static void close() {
        getCuratorFramework().close();
        zookeeperClose();
    }
}