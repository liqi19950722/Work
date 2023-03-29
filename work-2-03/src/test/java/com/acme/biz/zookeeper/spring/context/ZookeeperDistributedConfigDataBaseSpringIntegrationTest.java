package com.acme.biz.zookeeper.spring.context;

import com.acme.biz.zookeeper.distributedconfig.DistributedConfigDataBase;
import com.acme.biz.zookeeper.distributedconfig.event.DistributedConfigEventListener;
import com.acme.biz.zookeeper.distributedconfig.zookeeper.ZookeeperDistributedConfigDataBase;
import com.acme.biz.zookeeper.spring.context.event.SpringBridgeDistributedConfigEventListener;
import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.TimeUnit;

import static com.acme.biz.zookeeper.spring.context.ZookeeperDistributedConfigDataBaseSpringIntegrationTest.Constant.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {
        ZookeeperDistributedConfigDataBaseSpringIntegrationTest.class,
        DistributedConfigEnvironmentLifecycle.class,
        SpringBridgeDistributedConfigEventListener.class
})
class ZookeeperDistributedConfigDataBaseSpringIntegrationTest extends TestConfiguration {

    @Bean
    public DistributedConfigDataBase zookeeperDistributedConfigDataBase(@Autowired CuratorFramework curatorFramework,
                                                                        @Autowired ObjectProvider<DistributedConfigEventListener> objectProvider) {
        DistributedConfigDataBase zookeeperDistributedConfigDataBase = new ZookeeperDistributedConfigDataBase(curatorFramework);
        objectProvider.forEach(zookeeperDistributedConfigDataBase::registerListener);
        return zookeeperDistributedConfigDataBase;
    }

    @Autowired
    private Environment environment;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DistributedConfigDataBase distributedConfigDataBase;

    @BeforeAll
    public static void init() throws Exception {
        zookeeperStart();
        createCuratorFramework();

        var data = new DistributedConfigDataBase.ConfigValue<>(new DistributedConfigDataBase.ConfigValueMetadata("text", 1), "test");
        getCuratorFramework().create().creatingParentsIfNeeded().forPath("/distributed-config/application/default/distributed/config/test",
                DistributedConfigDataBase.JsonSerializer.serialize(data));
    }


    @Test
    public void should_set_property_from_distributed_config_via_spring_lifecycle() {
        var bean = applicationContext.getBean(DistributedConfigEnvironmentLifecycle.class);
        assertNotNull(bean);

        // /distributed-config/application/default/distributed/config/test
        var property = environment.getProperty(TEST_KEY);
        assertNotNull(property);
        assertEquals(TEST_VALUE, property);
    }

    @Test
    public void should_change_property_when_distributed_config_changed() throws InterruptedException {
        var oldValue = environment.getProperty(TEST_KEY);
        distributedConfigDataBase.putConfig(TEST_KEY, CHANGE_VALUE);
        TimeUnit.SECONDS.sleep(2L); // 等监听线程刷新配置

        var newValue = environment.getProperty(TEST_KEY);

        assertNotEquals(newValue, oldValue);
        assertEquals(newValue, CHANGE_VALUE);

    }

    @AfterAll
    public static void close() {
        getCuratorFramework().close();
        zookeeperClose();
    }

    interface Constant {
        String TEST_KEY = "distributed.config.test";
        String TEST_VALUE = "test";
        String CHANGE_VALUE = "change";
    }
}