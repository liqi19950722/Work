package com.acme.biz.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.DefaultTracerDriver;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.concurrent.TimeUnit;

import static com.acme.biz.zookeeper.ZookeeperContainerEnv.Constant.ZOOKEEPER_PORT;


public abstract class ZookeeperContainerEnv {
    @Container
    protected static final GenericContainer<?> ZOOKEEPER = new GenericContainer<>("zookeeper:3.8.0")
            .withAccessToHost(true)
            .withExposedPorts(ZOOKEEPER_PORT);

    protected static CuratorFramework curatorFramework;

    public static void zookeeperStart() {
        ZOOKEEPER.start();
    }

    public static void createCuratorFramework() throws Exception {
        String connectString = getConnectString();
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(60000)
                .connectionTimeoutMs(15000)
                .retryPolicy(new ExponentialBackoffRetry(50, 10, 500))
                .build();

        curatorFramework.getZookeeperClient().setTracerDriver(new DefaultTracerDriver());
        curatorFramework.start();
        curatorFramework.blockUntilConnected(0, TimeUnit.SECONDS);

    }

    public static CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }

    public static void zookeeperClose() {
        ZOOKEEPER.close();
    }
    public static String getConnectString() {
        return ZOOKEEPER.getHost() + ":" + ZOOKEEPER.getMappedPort(ZOOKEEPER_PORT);
    }

    interface Constant {
       int ZOOKEEPER_PORT = 2181;
    }
}
