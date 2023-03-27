package com.acme.biz.zookeeper.distributedconfig;

import com.acme.biz.zookeeper.distributedconfig.zookeeper.ZookeeperDistributedConfigDataBase;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.DefaultTracerDriver;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import static com.acme.biz.zookeeper.distributedconfig.DistributedConfigTest.Constant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DistributedConfigTest {

    @Container
    private static final GenericContainer<?> zookeeper = new GenericContainer<>("zookeeper:3.8.0")
            .withAccessToHost(true)
            .withExposedPorts(ZOOKEEPER_PORT);
    private static CuratorFramework curatorFramework;

    private static DistributedConfigDataBase distributedConfigDataBase;

    @BeforeAll
    public static void init() throws InterruptedException {
        zookeeper.start();
        String connectString = zookeeper.getHost() + ":" + zookeeper.getMappedPort(ZOOKEEPER_PORT);
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(60000)
                .connectionTimeoutMs(15000)
                .retryPolicy(new ExponentialBackoffRetry(50, 10, 500))
                .build();

        curatorFramework.getZookeeperClient().setTracerDriver(new DefaultTracerDriver());
        curatorFramework.start();
        curatorFramework.blockUntilConnected(10, TimeUnit.SECONDS);

        distributedConfigDataBase = new ZookeeperDistributedConfigDataBase(curatorFramework, CONFIG_NAMESPACE, APPLICATION_NAME);
    }

    // /{prefix}/{application}/{profile}/...
    // user.name  -> /config/application/default/user/name
    // user.age  -> /config/application/default/user/age

    // {"user":{"name":"张三","age":"18"}} -> /config/application/default/user/name /config/application/default/user/age

    @Test
    public void should_put_config_via_zookeeper_based_distributed_config() throws Exception {
        doPutConfigData("user.name", "张三", "/" + CONFIG_NAMESPACE + "/" + APPLICATION_NAME + "/default/user/name");
        doPutConfigData("user.age", 18, "/" + CONFIG_NAMESPACE + "/" + APPLICATION_NAME + "/default/user/age");
    }

    private static <T extends Serializable> void doPutConfigData(String key, T value, String path) throws Exception {
        distributedConfigDataBase.putConfig(key, value);
        Stat node = curatorFramework.checkExists().forPath(path);

        assertNotNull(node);
        assertEquals(value.getClass().cast(SerializationUtils.deserialize(curatorFramework.getData().forPath(path))), value);
    }

    @AfterAll
    public static void close() {
        curatorFramework.close();
        zookeeper.close();
    }

    interface Constant {
        int ZOOKEEPER_PORT = 2181;
        String CONFIG_NAMESPACE = "config";
        String APPLICATION_NAME = "application";
    }
}
